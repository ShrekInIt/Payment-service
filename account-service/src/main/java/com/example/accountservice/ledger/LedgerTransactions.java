package com.example.accountservice.ledger;

import com.example.accountservice.ledger.enums.LedgerTransactionsStatus;
import com.example.accountservice.ledger.enums.LedgerTransactionsType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record LedgerTransactions(
        UUID id,
        UUID referenceId,
        LedgerTransactionsType type,
        LedgerTransactionsStatus status,
        LocalDateTime createdAt
) {}
