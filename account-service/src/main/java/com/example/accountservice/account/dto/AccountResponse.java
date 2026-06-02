package com.example.accountservice.account.dto;

import com.example.accountservice.account.AccountCurrency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AccountResponse(
        UUID id,
        String ownerName,
        BigDecimal balance,
        AccountCurrency currency,
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {}
