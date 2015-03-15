package com.piotrkafel.rx.retrofit.client;

import com.google.gson.JsonObject;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

import java.util.UUID;

public interface AccountingClient {

    @GET("/line_item/{uuid}")
    public Observable<JsonObject> getContractLineItems(@Path("uuid") UUID uuid,
                                                       @Query("page") Integer page,
                                                       @Query("perPage") Integer perPage);
}
