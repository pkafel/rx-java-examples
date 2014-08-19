package com.piotrkafel.client;


import com.piotrkafel.model.UserData;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

import java.util.UUID;

public interface UsersClient {

    @Headers("Accept: application/json")
    @GET("/user/{user_id}")
    Observable<UserData> getUser(@Path("user_id") UUID userId);
}
