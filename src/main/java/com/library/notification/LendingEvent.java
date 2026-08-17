package com.library.notification;

import com.library.lending.LendingRecord;

import java.time.LocalDateTime;

public final class LendingEvent {

    private final LendingEventType type;
    private final LendingRecord record;
    private final LocalDateTime occurredAt;

    public LendingEvent(LendingEventType type, LendingRecord record) {
        this.type = type;
        this.record = record;
        this.occurredAt = LocalDateTime.now();
    }

    public LendingEventType getType() {
        return type;
    }

    public LendingRecord getRecord() {
        return record;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
