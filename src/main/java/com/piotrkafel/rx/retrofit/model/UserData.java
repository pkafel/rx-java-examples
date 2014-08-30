package com.piotrkafel.rx.retrofit.model;


import com.google.common.base.Objects;

import java.util.UUID;

public class UserData {

    private final UUID userId;

    private final String name;

    private final String address;

    // here you can put more data...

    public UserData(UUID userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("userId", userId)
                .add("name", name)
                .add("address", address)
                .toString();
    }
}
