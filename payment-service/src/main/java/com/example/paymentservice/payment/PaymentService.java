package com.example.paymentservice.payment;

import com.example.paymentservice.accountclient.AccountClient;
import com.example.paymentservice.payment.dto.CreatePaymentRequest;
import com.example.paymentservice.payment.dto.PaymentResponse;
import com.example.paymentservice.payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountClient accountClient;
    private final PaymentIdempotencyService idempotencyService;

    public PaymentResponse createPayment(String idempotencyKey, CreatePaymentRequest request)  {
        Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(idempotencyKey);

        if(existingPayment.isPresent()) return PaymentMapper.toResponse(existingPayment.get());

        boolean locked = idempotencyService.tryLock(idempotencyKey);

        if (!locked) {
            throw new IllegalStateException("Payment with this idempotency key is already processing");
        }

        log.info("Creating payment: {}", request);
        Payment payment = PaymentMapper.toPayment(request, idempotencyKey);
        paymentRepository.save(payment);

        try {
            accountClient.transfer(PaymentMapper.toAccountTransferRequest(payment));
            paymentRepository.updateStatus(payment.id(), PaymentStatus.SUCCEEDED, null, payment.version());
        } catch (Exception e) {
            log.error("Failed to update payment status for payment id {}: {}", payment.id(), e.getMessage());
            paymentRepository.updateStatus(payment.id(), PaymentStatus.FAILED, e.getMessage(), payment.version());
        }

        Payment updatedPayment = paymentRepository.findById(payment.id())
                .orElseThrow();
        idempotencyService.markCompleted(idempotencyKey, payment.id().toString());

        return PaymentMapper.toResponse(updatedPayment);
    }
}
