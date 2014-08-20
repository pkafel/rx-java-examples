package com.piotrkafel.rx.retrofit.model;

public class UserContact {

    private final Contact contact;

    private final UserData userData;

    public UserContact(Contact contact, UserData userData) {
        this.contact = contact;
        this.userData = userData;
    }

    public Contact getContact() {
        return contact;
    }

    public UserData getUserData() {
        return userData;
    }
}
