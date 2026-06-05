package com.example;

import jdk.jfr.BooleanFlag;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountTransferRequest(
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        Currency currency,
        UUID referenceId
) {}
