package com.example.accountservice.ledger;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LedgerRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveEntry(LedgerEntry ledgerEntries) {
        String sql = """
                INSERT INTO ledger_entries (id, transaction_id, account_id, direction, amount, currency, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
               """;

        jdbcTemplate.update(
                sql,
                ledgerEntries.id(),
                ledgerEntries.transactionId(),
                ledgerEntries.accountId(),
                ledgerEntries.direction().name(),
                ledgerEntries.amount(),
                ledgerEntries.currency().name(),
                ledgerEntries.createdAt()
        );
    }

    public void saveTransaction(LedgerTransactions transaction){
        String sql = """
                INSERT INTO ledger_transactions (id, reference_id, type, status, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                transaction.id(),
                transaction.referenceId(),
                transaction.type().name(),
                transaction.status().name(),
                transaction.createdAt()
        );
    }
}
