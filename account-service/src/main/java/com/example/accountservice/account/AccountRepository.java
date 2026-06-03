package com.example.accountservice.account;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(Account account) {
       String sql = """
                INSERT INTO
                    accounts (id, owner_name, balance, currency, created_at, updated_at, version)\s
                VALUES (?, ?, ?, ?, ?, ?, ?)
               """;
       jdbcTemplate.update(
               sql,
               account.id(),
               account.ownerName(),
               account.balance(),
               account.currency().name(),
               account.createdAt(),
               account.updatedAt(),
               account.version()
       );
    }

    public Optional<Account> findById(UUID id) {
       String sql = """
                SELECT id, owner_name, balance, currency, created_at, updated_at, version
                FROM accounts
                WHERE id = ?
               """;
        List<Account> accounts = jdbcTemplate.query(sql, ACCOUNT_ROW_MAPPER, id);

        if (accounts.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(accounts.getFirst());
    }

    public int debit(UUID id, BigDecimal amount, Long expectedVersion) {
       String sql = """
                UPDATE accounts
                SET balance = balance - ?,
                    version = version + 1,
                    updated_at = now()
                WHERE id = ?
                  AND version = ?
                  AND balance >= ?
               """;
       return jdbcTemplate.update(sql, amount, id, expectedVersion, amount);
    }

    public int credit(UUID id, BigDecimal amount, Long expectedVersion) {
       String sql = """
                UPDATE accounts
                SET balance = balance + ?,
                    version = version + 1,
                    updated_at = now()
                WHERE id = ?
                    AND version = ?
               """;
       return jdbcTemplate.update(sql, amount, id, expectedVersion);
    }

    private static final RowMapper<Account> ACCOUNT_ROW_MAPPER = (rs, rowNum) -> new Account(
            UUID.fromString(rs.getString("id")),
            rs.getString("owner_name"),
            rs.getBigDecimal("balance"),
            AccountCurrency.valueOf(rs.getString("currency")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime(),
            rs.getLong("version")
    );
}
