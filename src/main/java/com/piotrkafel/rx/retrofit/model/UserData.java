package com.piotrkafel.rx.retrofit.model;


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
        final StringBuilder sb = new StringBuilder("UserData{");
        sb.append("userId=").append(userId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
