package com.piotrkafel.rx.retrofit.client;

import com.piotrkafel.rx.retrofit.model.Contact;
import com.piotrkafel.rx.retrofit.model.Contacts;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

import java.util.List;
import java.util.UUID;

public interface ContactsClient {

    @Headers("Accept: application/json")
    @GET("/contact")
    Observable<Contacts> getContactsByUserId(@Query("user_id") UUID userId);

    @Headers("Accept: application/json")
    @GET("/contact?as_list=true")
    Observable<List<Contact>> getUnwrapedContactsByUserId(@Query("user_id") UUID userId);
}
