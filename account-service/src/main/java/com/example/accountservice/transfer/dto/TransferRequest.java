package com.example.accountservice.transfer.dto;

import com.example.accountservice.account.AccountCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotNull
        UUID fromAccountId,
        @NotNull
        UUID toAccountId,
        @NotNull
        @Positive
        BigDecimal amount,
        @NotNull
        AccountCurrency currency,
        @NotNull
        UUID referenceId
) {}
