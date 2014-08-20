package com.piotrkafel.rx.retrofit.client;

import com.piotrkafel.rx.retrofit.model.Contacts;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

import java.util.UUID;

public interface ContactsClient {

    @Headers("Accept: application/json")
    @GET("/contact/{contact_id}")
    Observable<Contacts> getContactsByUserId(@Path("contact_id") UUID contactId);
}
