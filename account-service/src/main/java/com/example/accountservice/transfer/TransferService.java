package com.example.accountservice.transfer;

import com.example.accountservice.account.Account;
import com.example.accountservice.account.AccountRepository;
import com.example.accountservice.exception.AccountNotFoundException;
import com.example.accountservice.exception.InsufficientFundsException;
import com.example.accountservice.ledger.LedgerRepository;
import com.example.accountservice.ledger.LedgerTransactions;
import com.example.accountservice.ledger.LedgerEntry;
import com.example.accountservice.ledger.LedgerEntriesDirection;
import com.example.accountservice.transfer.dto.TransferRequest;
import com.example.accountservice.transfer.dto.TransferResponse;
import com.example.accountservice.ledger.enums.LedgerTransactionsStatus;
import com.example.accountservice.ledger.enums.LedgerTransactionsType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {
    private final LedgerRepository ledgerRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Optional<LedgerTransactions> existing =
                ledgerRepository.findTransactionByReferenceId(request.referenceId());

        if (existing.isPresent()) {
            LedgerTransactions transaction = existing.get();

            return new TransferResponse(
                    transaction.id(),
                    request.fromAccountId(),
                    request.toAccountId(),
                    request.amount(),
                    request.currency(),
                    transaction.createdAt()
            );
        }

        Account fromAccount = accountRepository.findById(request.fromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("From account not found"));

        Account toAccount = accountRepository.findById(request.toAccountId())
                .orElseThrow(() -> new AccountNotFoundException("To account not found"));

        if(toAccount.currency() != fromAccount.currency()){
            throw new IllegalArgumentException("Currency mismatch between accounts");
        }

        int debitedRows = accountRepository.debit(request.fromAccountId(), request.amount(), fromAccount.version());

        if (debitedRows == 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        int creditedRows = accountRepository.credit(request.toAccountId(), request.amount(), toAccount.version());

        if (creditedRows == 0) {
            throw new AccountNotFoundException("To account not found");
        }

        UUID transactionId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        LedgerTransactions ledgerTransactions = LedgerTransactions.builder()
                .id(transactionId)
                .referenceId(request.referenceId())
                .type(LedgerTransactionsType.PAYMENT)
                .status(LedgerTransactionsStatus.POSTED)
                .createdAt(now)
                .build();

        LedgerEntry debitEntry = LedgerEntry.builder()
                .id(UUID.randomUUID())
                .transactionId(transactionId)
                .accountId(request.fromAccountId())
                .direction(LedgerEntriesDirection.DEBIT)
                .amount(request.amount())
                .currency(request.currency())
                .createdAt(now)
                .build();

        LedgerEntry creditEntry = LedgerEntry.builder()
                .id(UUID.randomUUID())
                .transactionId(transactionId)
                .accountId(request.toAccountId())
                .direction(LedgerEntriesDirection.CREDIT)
                .amount(request.amount())
                .currency(request.currency())
                .createdAt(now)
                .build();

        ledgerRepository.saveTransaction(ledgerTransactions);
        ledgerRepository.saveEntry(debitEntry);
        ledgerRepository.saveEntry(creditEntry);

        return new TransferResponse(
                transactionId,
                request.fromAccountId(),
                request.toAccountId(),
                request.amount(),
                request.currency(),
                now
        );
    }
}
