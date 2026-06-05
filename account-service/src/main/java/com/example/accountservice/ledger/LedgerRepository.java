package com.example.accountservice.ledger;

import com.example.accountservice.ledger.enums.LedgerTransactionsStatus;
import com.example.accountservice.ledger.enums.LedgerTransactionsType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<LedgerTransactions> findTransactionByReferenceId(UUID referenceId){
        String sql = """
                SELECT id, reference_id, type, status, created_at
                FROM ledger_transactions
                WHERE reference_id = ?
                """;

        List<LedgerTransactions> transactions =
                jdbcTemplate.query(sql, LEDGER_TRANSACTION_ROW_MAPPER, referenceId);

        if (transactions.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(transactions.getFirst());
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

    private static final RowMapper<LedgerTransactions> LEDGER_TRANSACTION_ROW_MAPPER =
            (rs, rowNum) -> new LedgerTransactions(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("reference_id")),
                    LedgerTransactionsType.valueOf(rs.getString("type")),
                    LedgerTransactionsStatus.valueOf(rs.getString("status")),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
}
