package com.piotrkafel.rx.retrofit.model;


import java.util.ArrayList;
import java.util.List;

public class Contacts {

    private final List<Contact> contacts;

    public Contacts(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }
}
