package com.example.accountservice.account;

import com.example.accountservice.account.dto.AccountResponse;
import com.example.accountservice.account.dto.CreateAccountRequest;
import com.example.accountservice.account.dto.MoneyOperationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody @Valid CreateAccountRequest request
    ){
        log.info("Received request to create account: {}", request);
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID id
    ) {
        log.info("Received request to get account by ID: {}", id);
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/debit")
    public ResponseEntity<AccountResponse> debitAccount(
            @PathVariable UUID id,
            @RequestBody @Valid MoneyOperationRequest request
            ) {
        log.info("Received request to debit account: id={}, amount={}", id, request);
        AccountResponse response = accountService.debit(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/credit")
    public ResponseEntity<AccountResponse> creditAccount(
            @PathVariable UUID id,
            @RequestBody @Valid MoneyOperationRequest request
    ) {
        log.info("Received request to credit account: id={}, amount={}", id, request);
        AccountResponse response = accountService.credit(id, request);
        return ResponseEntity.ok(response);
    }
}
