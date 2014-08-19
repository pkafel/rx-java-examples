package com.piotrkafel.model;

import java.util.List;

public class UserProfile {

    private final UserData userData;

    private final List<UserContact> userContacts;

    public UserProfile(UserData userData, UserContacts userContacts) {
        this.userData = userData;
        this.userContacts = userContacts.getUserContacts();
    }

    public UserData getUserData() {
        return userData;
    }

    public List<UserContact> getUserContacts() {
        return userContacts;
    }
}
