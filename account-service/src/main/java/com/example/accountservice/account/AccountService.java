package com.example.accountservice.account;

import com.example.accountservice.account.dto.AccountResponse;
import com.example.accountservice.account.dto.CreateAccountRequest;
import com.example.accountservice.account.dto.MoneyOperationRequest;
import com.example.accountservice.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO;
    private final AccountRepository accountRepository;

    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account with name: {}", request.ownerName());

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Account account = new Account(
                id,
                request.ownerName(),
                INITIAL_BALANCE,
                request.currency(),
                now,
                now,
                0L
        );

        accountRepository.save(account);

        return AccountMapper.toAccountResponse(account);
    }

    public AccountResponse debit(UUID id, MoneyOperationRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + id));

        int updatedRows = accountRepository.debit(id, request.amount(), account.version());

        if (updatedRows == 0) {
            throw new IllegalArgumentException("Account not found or insufficient funds");
        }

        return getAccountById(id);
    }

    public AccountResponse credit(UUID id, MoneyOperationRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + id));

        int updatedRows = accountRepository.credit(id, request.amount(), account.version());

        if (updatedRows == 0) {
            throw new IllegalArgumentException("Account not found or insufficient funds");
        }

        return getAccountById(id);
    }

    public AccountResponse getAccountById(UUID accountId) {
        log.info("Retrieving account with ID: {}", accountId);
        return AccountMapper.toAccountResponse(accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId)));
    }
}
