package com.piotrkafel.rx.retrofit.client;

import com.google.gson.JsonObject;
import rx.Observable;

import java.util.UUID;

public interface AccountingClient {

    public Observable<JsonObject> getContractLineItems(UUID uuid, Integer page, Integer perPage);
}
