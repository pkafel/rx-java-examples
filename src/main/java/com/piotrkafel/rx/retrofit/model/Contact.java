package com.piotrkafel.rx.retrofit.model;


import java.time.ZonedDateTime;
import java.util.UUID;

public class Contact {

    private final UUID contactId;

    private final UUID userId;

    private final boolean isActive;

    // here you can put more data...

    public Contact(UUID contactId, UUID userId, boolean isActive) {
        this.contactId = contactId;
        this.userId = userId;
        this.isActive = isActive;
    }

    public UUID getContactId() {
        return contactId;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }
}
