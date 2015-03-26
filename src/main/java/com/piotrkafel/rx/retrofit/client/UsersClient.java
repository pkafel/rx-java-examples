package com.piotrkafel.rx.retrofit.client;


import com.piotrkafel.rx.retrofit.model.GroupsOfUsers;
import com.piotrkafel.rx.retrofit.model.UserData;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

import java.util.List;
import java.util.UUID;

public interface UsersClient {

    @Headers("Accept: application/json")
    @GET("/user/{user_id}")
    Observable<UserData> getUser(@Path("user_id") UUID userId);

    @Headers("Accept: application/json")
    @GET("/user")
    Observable<List<UserData>> getUsers(@Query("user_ids") String userIds);

    @Headers("Accept: application/json")
    @GET("/group")
    Observable<GroupsOfUsers> getGroups(@Query("page") Integer page, @Query("perPage") Integer perPage);

    @Headers("Accept: application/json")
    @GET("/group/{group_id}")
    Observable<UserData> getGroupLeader(@Path("group_id") Integer integer);
}
