package com.example.accountservice.transfer.dto;

import com.example.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(
        UUID transactionId,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        Currency currency,
        LocalDateTime createdAt
) {}
