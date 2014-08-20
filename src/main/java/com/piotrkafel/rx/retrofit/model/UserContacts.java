package com.piotrkafel.rx.retrofit.model;


import java.util.ArrayList;
import java.util.List;

public class UserContacts {

    private final List<UserContact> userContacts;

    public UserContacts(List<UserContact> userContacts) {
        this.userContacts = new ArrayList<>(userContacts);
    }

    public List<UserContact> getUserContacts() {
        return new ArrayList<>(userContacts);
    }
}
