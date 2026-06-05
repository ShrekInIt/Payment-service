package com.example.paymentservice.outbox;

public enum OutboxEventStatus {
    NEW,
    PUBLISHED,
    FAILED
}
