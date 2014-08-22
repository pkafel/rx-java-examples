package com.piotrkafel.rx.retrofit;


import com.github.restdriver.clientdriver.ClientDriverRule;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.piotrkafel.rx.retrofit.model.Contact;
import com.piotrkafel.rx.retrofit.model.Contacts;
import com.piotrkafel.rx.retrofit.model.UserData;
import com.piotrkafel.rx.retrofit.model.UserProfile;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;

public class RetrofitExampleTest {

    private static final int port = 1234;
    private static final String host = "http://localhost:" + port;
    private static final Gson gson = new Gson();

    @Rule
    public final ClientDriverRule clientDriver = new ClientDriverRule(port);

    @Test
    public void getContactsTest() {
        // Given
        final UUID userId = UUID.randomUUID();
        setupServerResponses(userId);

        // When
        final UserProfile userProfile = new RetrofitExample(host).getUserProfileExample(userId);

        // Then
        Assert.assertNotNull(userProfile);
    }

    private void setupServerResponses(UUID userId) {
        final UUID userId1 = UUID.randomUUID();
        final UUID userId2 = UUID.randomUUID();

        clientDriver.addExpectation(onRequestTo("/user/" + userId).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId, "Eddie Vedder", "somewhere in US...")), "application/json"));

        clientDriver.addExpectation(onRequestTo("/contact/" + userId).withMethod(GET),
                giveResponse(gson.toJson(new Contacts(Lists.newArrayList(
                        new Contact(UUID.randomUUID(), userId1, true), new Contact(UUID.randomUUID(), userId2, true)
                        ))), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user/" + userId1).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId1, "Mick McCreedy", "Also in US...")), "application/json"));

        clientDriver.addExpectation(onRequestTo("/user/" + userId2).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId2, "Stone Gossard", "Also in US...")), "application/json"));
    }
}
