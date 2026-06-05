package com.example.accountservice.account;

import com.example.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Account(
        UUID id,
        String ownerName,
        BigDecimal balance,
        Currency currency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {}
