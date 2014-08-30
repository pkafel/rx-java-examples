package com.piotrkafel.rx.retrofit;


import com.github.restdriver.clientdriver.ClientDriverRule;
import com.github.restdriver.clientdriver.HttpRealRequest;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.piotrkafel.rx.retrofit.model.Contact;
import com.piotrkafel.rx.retrofit.model.Contacts;
import com.piotrkafel.rx.retrofit.model.UserData;
import com.piotrkafel.rx.retrofit.model.UserProfile;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static java.lang.String.format;
import static org.junit.Assert.*;

public class RetrofitExampleTest {

    private static final int port = 1234;
    private static final String host = "http://localhost:" + port;
    private static final Gson gson = new Gson();

    @Rule
    public final ClientDriverRule clientDriver = new ClientDriverRule(port);

    @Test
    public void getUserProfileTest() {
        // Given
        final UUID userId = UUID.randomUUID();
        setupServerResponses(userId);

        // When
        final UserProfile userProfile = new RetrofitExample(host).getUserProfileExample(userId);

        // Then
        assertNotNull(userProfile);
    }

    @Test
    public void getUserContactsTest() {
        // Given
        final int batchSize = 2;
        final UUID userId = UUID.randomUUID();
        setupServer(userId, batchSize);

        // When
        final List<UserData> contacts = new RetrofitExample(host).getContactsDataByBatchRequests(userId, batchSize);

        // Then
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
    }

    private void setupServer(UUID userId, int batchSize) {
        final UUID userId2 = UUID.randomUUID();
        final UUID userId3 = UUID.randomUUID();
        final UUID userId4 = UUID.randomUUID();

        final ArrayList<Contact> contacts1 = Lists.newArrayList(
                new Contact(UUID.randomUUID(), userId2, true),
                new Contact(UUID.randomUUID(), userId3, true),
                new Contact(UUID.randomUUID(), userId4, true)
        );

        clientDriver.addExpectation(onRequestTo("/contact").withMethod(GET).withParam("user_id", userId).withParam("as_list", true),
                giveResponse(gson.toJson(contacts1), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user").withMethod(GET).withParam("user_ids", "" + userId2 + ","  + userId3),
                giveResponse(gson.toJson(Lists.newArrayList(new UserData(userId2, "A", "B"), new UserData(userId3, "C", "D"))), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user").withMethod(GET).withParam("user_ids", userId4),
                giveResponse(gson.toJson(Lists.newArrayList(new UserData(userId4, "E", "F"))), "application/json"));
    }

    private void setupServerResponses(UUID userId) {
        final UUID userId1 = UUID.randomUUID();
        final UUID userId2 = UUID.randomUUID();

        clientDriver.addExpectation(onRequestTo("/user/" + userId).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId, "Eddie Vedder", "somewhere in US...")), "application/json"));

        clientDriver.addExpectation(onRequestTo("/contact").withMethod(GET).withParam("user_id", userId),
                giveResponse(gson.toJson(new Contacts(Lists.newArrayList(
                        new Contact(UUID.randomUUID(), userId1, true), new Contact(UUID.randomUUID(), userId2, true)
                        ))), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user/" + userId1).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId1, "Mick McCreedy", "Also in US...")), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user/" + userId2).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId2, "Stone Gossard", "Also in US")), "application/json"));
    }
}
