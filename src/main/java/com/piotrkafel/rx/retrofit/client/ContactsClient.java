package com.piotrkafel.rx.retrofit.client;

import com.piotrkafel.rx.retrofit.model.Contacts;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

import java.util.UUID;

public interface ContactsClient {

    @Headers("Accept: application/json")
    @GET("/contact")
    Observable<Contacts> getContactsByUserId(@Query("user_id") UUID userId);
}
