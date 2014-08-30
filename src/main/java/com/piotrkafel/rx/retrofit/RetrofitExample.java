package com.piotrkafel.rx.retrofit;


import com.google.common.base.Joiner;
import com.piotrkafel.rx.retrofit.client.ContactsClient;
import com.piotrkafel.rx.retrofit.client.UsersClient;
import com.piotrkafel.rx.retrofit.model.UserContact;
import com.piotrkafel.rx.retrofit.model.UserContacts;
import com.piotrkafel.rx.retrofit.model.UserData;
import com.piotrkafel.rx.retrofit.model.UserProfile;
import retrofit.RestAdapter;
import rx.Observable;
import rx.functions.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Example of using RxJava with Retrofit
 */
public class RetrofitExample {

    private static final Joiner JOINER = Joiner.on(",");

    private final ContactsClient contactClient;

    private final UsersClient userClient;

    public RetrofitExample(String host) {
        this.contactClient = new RestAdapter.Builder().setEndpoint(host).build().create(ContactsClient.class);
        this.userClient = new RestAdapter.Builder().setEndpoint(host).build().create(UsersClient.class);
    }

    public UserProfile getUserProfileExample(UUID userId) {

        final Observable<UserData> requestedUserData = userClient.getUser(userId);

        final Observable<UserContacts> requestedUserContacts = contactClient.getContactsByUserId(userId)    // get user's contacts
                .flatMap( contacts -> Observable.from(contacts.getContacts()) )                             // unwrap contacts object so we will have Observable that emits many items
                .mergeMap( contact -> userClient.getUser(contact.getUserId()),                              // for each contact call user client in order to get details
                        (contact, userData) -> new UserContact(contact, userData) )                         // and zip contact with user data (or do with it whatever you like)
                .toList()                                                                                   // transform back to list
                .map( userContactsList -> new UserContacts(userContactsList) );                             // wrap list of user contacts into UserContacts object

        return Observable.zip(requestedUserData, requestedUserContacts, (userData, userContacts) -> new UserProfile(userData, userContacts) )
                .toBlocking().single(); // zip requested user data with all its contacts
    }

    public List<UserData> getContactsDataByBatchRequests(UUID userID, int batchSize) {

        return contactClient.getUnwrapedContactsByUserId(userID)                    // get users contacts
                .mergeMapIterable(Functions.identity())                             // unwrap list of contacts so we will have Observable that emits many items
                .map(contact -> contact.getUserId().toString())                     // transform each contact into user id
                .buffer(batchSize)                                                  // group user ids
                .flatMap(contacts -> userClient.getUsers(JOINER.join(contacts)))    // call user service for users data
                .reduce(new ArrayList<UserData>(), (accu, usersData) -> {           // reduce to list
                    accu.addAll(usersData); return accu;
                })
                .toBlocking().single();
    }
}
