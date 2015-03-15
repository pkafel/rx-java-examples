package com.piotrkafel.rx.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.piotrkafel.rx.retrofit.client.AccountingClient;
import retrofit.RestAdapter;
import rx.Observable;

import java.util.UUID;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class CacheExample {

    private static final String LINE_ITEMS_PROPERTY = "lineItems";

    private AccountingClient accountingClient = new RestAdapter.Builder()
            .setExecutors(newFixedThreadPool(10), newFixedThreadPool(10))
            .build()
            .create(AccountingClient.class);


    /**
     * This is more or less how code looks right now. As you can see there is a use of Rx but it is rather poor...
     */
    public JsonObject beforeRefactoring(UUID contractUuid, Integer page, Integer perPage) {
        JsonObject response = accountingClient.getContractLineItems(contractUuid, page, perPage).toBlocking().single();

        final JsonElement pagination = response.get("pagination");
        final JsonElement result = Observable.just(response)
                .flatMap(lineItems -> Observable.from(lineItems.getAsJsonArray("lineItems")))
                .flatMap(this::getContactEarnings)
                .reduce(new JsonArray(), (JsonArray jsonE, JsonObject jsonA) -> {
                    jsonE.add(jsonE);
                    return jsonE;
                }).cast(JsonElement.class).toBlocking().single();

        JsonObject output = new JsonObject();
        output.add(LINE_ITEMS_PROPERTY, result);
        output.add("pagination", pagination);
        return output;
    }

    /**
     * This is what I would like to have - no toBlocking() except the last line of code
     */
    public JsonObject afterRefactoring(UUID contractUuid, Integer page, Integer perPage) {
        Observable<JsonObject> response = accountingClient.getContractLineItems(contractUuid, page, perPage).cache();

        final Observable<JsonElement> pagination = response.map(jh -> jh.get("pagination"));
        final Observable<JsonElement> result = response
                .flatMap(lineItems -> Observable.from(lineItems.getAsJsonArray("lineItems")))
                .flatMap(this::getContactEarnings)
                .reduce(new JsonArray(), (JsonArray jsonE, JsonObject jsonA) -> {
                    jsonE.add(jsonE);
                    return jsonE;
                }).cast(JsonElement.class);

        return zipResultAndPagination(result, LINE_ITEMS_PROPERTY, pagination).toBlocking().single();
    }

    private Observable<JsonObject> getContactEarnings(JsonElement lineItem) {
        // code omitted for sake of simplicity but here should be another bunch of calls to different APIs
        return Observable.just(new JsonObject());
    }

    private Observable<JsonObject> zipResultAndPagination(Observable<JsonElement> result, String resultPath, Observable<JsonElement> pagination) {
        return Observable.zip(result, pagination, (r, p) -> {
            JsonObject o = new JsonObject();
            o.add(resultPath, r);
            o.add("pagination", p);
            return o;
        });
    }
}
