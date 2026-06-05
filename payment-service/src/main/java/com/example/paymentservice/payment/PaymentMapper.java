package com.example.paymentservice.payment;

import com.example.AccountTransferRequest;
import com.example.paymentservice.payment.dto.CreatePaymentRequest;
import com.example.paymentservice.payment.dto.PaymentResponse;
import com.example.paymentservice.payment.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public final class PaymentMapper {
    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.id(),
                payment.fromAccountId(),
                payment.toAccountId(),
                payment.amount(),
                payment.currency(),
                payment.status(),
                payment.failureReason(),
                payment.createdAt(),
                payment.updatedAt()
        );
    }

    public static Payment toPayment(CreatePaymentRequest request, String idempotencyKey) {
        return Payment.builder()
                .id(UUID.randomUUID())
                .fromAccountId(request.fromAccountId())
                .toAccountId(request.toAccountId())
                .amount(request.amount())
                .currency(request.currency())
                .status(PaymentStatus.PROCESSING)
                .failureReason(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .idempotencyKey(idempotencyKey)
                .version(0L)
                .build();
    }

    public static AccountTransferRequest toAccountTransferRequest(Payment payment) {
        return new AccountTransferRequest(
                payment.fromAccountId(),
                payment.toAccountId(),
                payment.amount(),
                payment.currency(),
                payment.id()
        );
    }
}
