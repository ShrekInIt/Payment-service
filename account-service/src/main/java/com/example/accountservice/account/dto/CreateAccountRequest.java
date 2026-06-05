package com.example.accountservice.account.dto;

import com.example.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateAccountRequest(
        @NotBlank
        String ownerName,
        @NotNull
        Currency currency
) {}
