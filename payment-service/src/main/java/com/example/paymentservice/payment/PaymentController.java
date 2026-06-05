package com.example.paymentservice.payment;

import com.example.paymentservice.payment.dto.CreatePaymentRequest;
import com.example.paymentservice.payment.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody @Valid CreatePaymentRequest request
    ){
        PaymentResponse response = paymentService.createPayment(idempotencyKey,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
