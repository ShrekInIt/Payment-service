package com.example.accountservice.transfer.dto;

import com.example.accountservice.account.AccountCurrency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(
        UUID transactionId,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        AccountCurrency currency,
        LocalDateTime createdAt
) {}
