package com.piotrkafel.rx.retrofit;


import com.github.restdriver.clientdriver.ClientDriverRule;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.piotrkafel.rx.retrofit.model.Contact;
import com.piotrkafel.rx.retrofit.model.Contacts;
import com.piotrkafel.rx.retrofit.model.UserData;
import com.piotrkafel.rx.retrofit.model.UserProfile;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RetrofitExampleTest {

    private static final int port = 1234;
    private static final String host = "http://localhost:" + port;
    private static final Gson gson = new Gson();

    @Rule
    public final ClientDriverRule clientDriver = new ClientDriverRule(port);

    private final RetrofitExample example = new RetrofitExample(host);

    @Test
    public void getUserProfileTest() {
        // Given
        final UUID userId = UUID.randomUUID();
        setupServerResponsesForGetUserProfile(userId);

        // When
        final UserProfile userProfile = example.getUserProfileExample(userId);

        // Then
        assertNotNull(userProfile);
    }

    @Test
    public void getUserContactsTest() {
        // Given
        final UUID userId = UUID.randomUUID();
        final int batchSize = setupServerResponsesForGetContactsByBatchCalls(userId);

        // When
        final List<UserData> contacts = example.getContactsDataByBatchRequests(userId, batchSize);

        // Then
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
    }

    private void setupServerResponsesForGetUserProfile(UUID userId) {
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

    private int setupServerResponsesForGetContactsByBatchCalls(UUID userId) {
        final int batchSize = 2;
        final UUID userId2 = UUID.randomUUID(),
                   userId3 = UUID.randomUUID(),
                   userId4 = UUID.randomUUID();

        final ArrayList<Contact> contacts = Lists.newArrayList(
                new Contact(UUID.randomUUID(), userId2, true),
                new Contact(UUID.randomUUID(), userId3, true),
                new Contact(UUID.randomUUID(), userId4, true)
        );

        final Joiner joiner = Joiner.on(",");

        clientDriver.addExpectation(onRequestTo("/contact").withMethod(GET).withParam("user_id", userId).withParam("as_list", true),
                giveResponse(gson.toJson(contacts), "application/json"));


        clientDriver.addExpectation(onRequestTo("/user").withMethod(GET).withParam("user_ids", joiner.join(userId2, userId3)),
                giveResponse(gson.toJson(Lists.newArrayList(new UserData(userId2, "Mat Cameron", "US"),
                        new UserData(userId3, "Jeff Ament", "US"))), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user").withMethod(GET).withParam("user_ids", userId4),
                giveResponse(gson.toJson(Lists.newArrayList(new UserData(userId4, "Boom Gaspar", "US"))), "application/json"));

        return batchSize;
    }
}
