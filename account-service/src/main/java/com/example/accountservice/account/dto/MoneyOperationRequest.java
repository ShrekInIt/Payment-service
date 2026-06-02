package com.example.accountservice.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MoneyOperationRequest(
        @NotNull
        @Positive
        BigDecimal amount
) {}
