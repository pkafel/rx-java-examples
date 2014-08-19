package com.piotrkafel.model;


import java.util.List;

public class UserContacts {

    private final List<UserContact> userContacts;

    public UserContacts(List<UserContact> userContacts) {
        this.userContacts = userContacts;
    }

    public List<UserContact> getUserContacts() {
        return userContacts;
    }
}
