package com.example.accountservice.ledger;

import com.example.accountservice.account.AccountCurrency;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record LedgerEntry(
        UUID id,
        UUID transactionId,
        UUID accountId,
        LedgerEntriesDirection direction,
        BigDecimal amount,
        AccountCurrency currency,
        LocalDateTime createdAt
) {}
