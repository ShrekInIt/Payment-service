package com.example.paymentservice.payment;

import com.example.Currency;
import com.example.paymentservice.payment.enums.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record Payment(
    UUID id,
    UUID fromAccountId,
    UUID toAccountId,
    BigDecimal amount,
    Currency currency,
    PaymentStatus status,
    String failureReason,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String idempotencyKey,
    Long version
) {}
