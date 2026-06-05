package com.example.paymentservice.outbox;

import java.time.LocalDateTime;
import java.util.UUID;

public record OutboxEvent(
        UUID id ,
        String aggregate_type,
        UUID aggregate_id,
        String event_type,
        String payload,
        OutboxEventStatus status,
        LocalDateTime created_at,
        LocalDateTime published_at
) {}
