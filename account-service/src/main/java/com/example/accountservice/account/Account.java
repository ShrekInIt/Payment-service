package com.example.accountservice.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Account(
        UUID id,
        String ownerName,
        BigDecimal balance,
        AccountCurrency currency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
