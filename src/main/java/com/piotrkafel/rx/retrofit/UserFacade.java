package com.piotrkafel.rx.retrofit;


import com.piotrkafel.rx.retrofit.client.ContactsClient;
import com.piotrkafel.rx.retrofit.client.UsersClient;
import com.piotrkafel.rx.retrofit.model.*;
import retrofit.RestAdapter;
import rx.Observable;

import java.util.*;

/**
 * Example of using RxJava with Retrofit
 */
public class UserFacade {

    private final ContactsClient contactClient;

    private final UsersClient userClient;

    public UserFacade(String host) {
        this.contactClient = new RestAdapter.Builder().setEndpoint(host).build().create(ContactsClient.class);
        this.userClient = new RestAdapter.Builder().setEndpoint(host).build().create(UsersClient.class);
    }

    public UserProfile getUserProfileExample(UUID userId) {

        final Observable<UserData> requestedUserData = userClient.getUser(userId);

        final Observable<UserContacts> requestedUserContacts = contactClient.getContactsByUserId(userId)    // get user's contacts
                .flatMap(contacts -> Observable.from(contacts.getContacts()))                               // unwrap contacts object so we will have Observable that emits many items
                .flatMap(contact -> userClient.getUser(contact.getUserId()), UserContact::new)              // for each contact call user client and zip result with contact object
                .toList()                                                                                   // transform back to list
                .map(UserContacts::new);                                                                    // wrap list of user contacts into UserContacts object

        return Observable.zip(requestedUserData, requestedUserContacts, UserProfile::new)
                .toBlocking().single(); // zip requested user data with all its contacts
    }

    public List<UserData> getContactsDataByBatchRequests(UUID userID, int batchSize) {

        return contactClient.getUnwrapedContactsByUserId(userID)                        // get users contacts
                .flatMapIterable(list -> list)                                          // unwrap list of contacts so we will have Observable that emits many items
                .map(contact -> contact.getUserId().toString())                         // transform each contact into user id
                .buffer(batchSize)                                                      // group user ids
                .flatMap(contacts -> userClient.getUsers(String.join(",", contacts)))   // call user service for users data
                .reduce(new ArrayList<UserData>(), (accu, usersData) -> {               // reduce to list
                    accu.addAll(usersData);
                    return accu;
                })
                .toBlocking().single();
    }

    public Map<String, Object> getGroupsLeaders(Integer page, Integer perPage) {
        Observable<GroupsOfUsers> response = userClient.getGroups(page, perPage).cache();

        final Observable<Pagination> pagination = response.map(GroupsOfUsers::getPagination);
        final Observable<List<UserData>> result = response
                .flatMapIterable(GroupsOfUsers::getGroupIds)
                .flatMap(userClient::getGroupLeader)
                .toList();

        return Observable.zip(result, pagination, (res, pag) -> {
            final Map<String, Object> output = new HashMap<>();
            output.put("result", res);
            output.put("pagination", pag);
            return output;
        }).toBlocking().single();
    }
}
