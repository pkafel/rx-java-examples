package com.piotrkafel.rx.retrofit;


import com.github.restdriver.clientdriver.ClientDriverRule;
import com.google.gson.Gson;
import com.piotrkafel.rx.retrofit.client.UsersClient;
import com.piotrkafel.rx.retrofit.model.UserData;
import org.junit.Rule;
import org.junit.Test;
import retrofit.RestAdapter;
import rx.Observable;

import java.util.UUID;
import java.util.concurrent.Executors;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Map.Entry;
import static org.junit.Assert.assertEquals;

public class ComplexObservableTest {

    private static final String host = "http://localhost:8080";

    @Rule
    public final ClientDriverRule clientDriver = new ClientDriverRule(8080);

    private static final Gson gson = new Gson();

    private final UsersClient userClient = new RestAdapter.Builder()
            .setEndpoint(host)
            .setExecutors(Executors.newFixedThreadPool(10), Executors.newFixedThreadPool(10))
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build()
            .create(UsersClient.class);



    @Test
    public void isCacheThreadSafe() {
        final UUID userId = UUID.fromString("1a8fbec1-23ad-4fa8-9bb3-c9092dd4048f");
        final String name = "Eddie Vedder";
        final String address = "somewhere in US...";
        clientDriver.addExpectation(onRequestTo("/user/" + userId).withMethod(GET),
                giveResponse(gson.toJson(new UserData(userId, name, address)), "application/json"));

        final Observable<UserData> single = userClient.getUser(userId).cache();

        final Observable<String> nameObservable = single.map(UserData::getName);
        final Observable<String> addressObservable = single.map(UserData::getAddress);

        final Entry<String, String> result = Observable.zip(nameObservable, addressObservable,
                (n, a) -> new SimpleImmutableEntry<>(n, a)).toBlocking().single();
        assertEquals(name, result.getKey());
        assertEquals(address, result.getValue());
    }
}
