package com.piotrkafel.model;


import java.time.ZonedDateTime;
import java.util.UUID;

public class Contact {

    private final UUID contactId;

    private final UUID userId;

    private final boolean isActive;

    private final ZonedDateTime createdAt;

    public Contact(UUID contactId, UUID userId, boolean isActive, ZonedDateTime dateTime) {
        this.contactId = contactId;
        this.userId = userId;
        this.isActive = isActive;
        this.createdAt = dateTime;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
