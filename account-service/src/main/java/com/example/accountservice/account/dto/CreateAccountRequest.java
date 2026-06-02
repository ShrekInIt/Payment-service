package com.example.accountservice.account.dto;

import com.example.accountservice.account.AccountCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateAccountRequest(
        @NotBlank
        String ownerName,
        @NotNull
        AccountCurrency currency
) {}
