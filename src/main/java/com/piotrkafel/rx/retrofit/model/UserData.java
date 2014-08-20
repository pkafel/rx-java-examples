package com.piotrkafel.rx.retrofit.model;


import java.util.UUID;

public class UserData {

    private final UUID userId;

    private final String name;

    private final String address;

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
}
