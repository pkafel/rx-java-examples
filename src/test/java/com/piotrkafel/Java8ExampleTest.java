package com.piotrkafel;


import com.github.restdriver.clientdriver.ClientDriverRequest;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.google.common.collect.Lists;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.piotrkafel.client.ContactsClient;
import com.piotrkafel.client.UsersClient;
import com.piotrkafel.model.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import retrofit.RestAdapter;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.List;
import java.util.UUID;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.*;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;

public class Java8ExampleTest {

    private static final int port = 1234;
    private static final String host = "http://localhost:" + port;

    @Rule
    public final ClientDriverRule clientDriver = new ClientDriverRule(port);

    /**
     * Contacts client has access to contacts of user
     */
    private final ContactsClient contactClient = new RestAdapter.Builder().setEndpoint(host).
            build().create(ContactsClient.class);

    /**
     * UserClient has access to user data.
     */
    private final UsersClient userClient = new RestAdapter.Builder().setEndpoint(host).
            build().create(UsersClient.class);

    private final Func1<Contacts, Observable<Contact>> unwrapContactsJsonHolder = contacts -> Observable.from(contacts.getContacts());

    public final Func2<Contact, UserData, UserContact> toUsersServiceAndMasOutput = (jsonHolder, jsonElement) -> new UserContact(jsonHolder, jsonElement);

    private final Func1<Contact, Observable<UserData>> callUserService = contact -> userClient.getUser(contact.getUserId());

    private final Func2<UserData, UserContacts, UserProfile> TO_USER_PROFILE = (userData, userContacts) -> new UserProfile(userData, userContacts);

    private final Func1<List<UserContact>, UserContacts> WRAP_INTO_USER_CONTACTS = userContacts -> new UserContacts(userContacts);

    @Test
    public void getContactsTest() {
        // Given
        final UUID userId = UUID.randomUUID();
        setupServerResponses(userId);

        // When
        final Observable<UserData> userData = userClient.getUser(userId);
        final Observable<UserContacts> userContacts = contactClient.getContactsByUserId(userId)
                .flatMap(unwrapContactsJsonHolder)
                .mergeMap(callUserService, toUsersServiceAndMasOutput)
                .toList()
                .map(WRAP_INTO_USER_CONTACTS);

        final UserProfile userProfile = Observable.zip(userData, userContacts, TO_USER_PROFILE).toBlocking().single();

        // Then
        Assert.assertNotNull(userProfile);
    }

    private void setupServerResponses(UUID userId) {
        final Gson gson = new Gson();
        final UUID userId1 = UUID.randomUUID();
        final UUID userId2 = UUID.randomUUID();

        clientDriver.addExpectation(onRequestTo("/user/" + userId).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId, "Eddie Vedder", "somewhere in US...")), "application/json"));

        clientDriver.addExpectation(onRequestTo("/contact/" + userId).withMethod(GET),
                giveResponse(gson.toJson(new Contacts(Lists.newArrayList(
                        new Contact(UUID.randomUUID(), userId1, true, null), new Contact(UUID.randomUUID(), userId2, true, null)
                        ))), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user/" + userId1).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId1, "Mick McCreedy", "Also in US...")), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user/" + userId2).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId2, "Stone Gossard", "Also in US...")), "application/json"));
    }
}
