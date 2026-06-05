package com.example.paymentservice.payment;

import com.example.Currency;
import com.example.paymentservice.payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {
    private final JdbcTemplate jdbcTemplate;

    public Payment save(Payment payment) {
        String sql = """
                INSERT INTO payments (id, from_account_id, to_account_id, amount, currency, status, failure_reason, created_at, updated_at, idempotency_key,version)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                payment.id(),
                payment.fromAccountId(),
                payment.toAccountId(),
                payment.amount(),
                payment.currency().name(),
                payment.status().name(),
                payment.failureReason(),
                payment.createdAt(),
                payment.updatedAt(),
                payment.idempotencyKey(),
                payment.version());

        return payment;
    }

    public Optional<Payment> findById(UUID id) {
        String sql = "SELECT * FROM payments WHERE id = ?";

        List<Payment> payments = jdbcTemplate.query(sql, PAYMENT_ROW_MAPPER, id);

        if (payments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(payments.getFirst());
    }

    public void updateStatus(UUID id, PaymentStatus status, String failureReason, Long version){
        String sql = """
                UPDATE payments
                SET status = ?,
                    failure_reason = ?,
                    version = version + 1,
                    updated_at = now()
                WHERE id = ?
                  AND version = ?
                """;

        int rowsAffected = jdbcTemplate.update(sql, status.name(), failureReason, id, version);

        if (rowsAffected == 0) {
            throw new IllegalStateException("Failed to update payment status. Payment might have been modified by another transaction.");
        }
    }

    public Optional<Payment> findByIdempotencyKey(String idempotencyKey){
        String sql = """
                SELECT * FROM payments WHERE idempotency_key = ?
                """;

        List<Payment> payments = jdbcTemplate.query(sql, PAYMENT_ROW_MAPPER, idempotencyKey);

        if (payments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(payments.getFirst());
    }

    private static final RowMapper<Payment> PAYMENT_ROW_MAPPER =
            (rs, rowNum) -> new Payment(
                    UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("from_account_id")),
                    UUID.fromString(rs.getString("to_account_id")),
                    rs.getBigDecimal("amount"),
                    Currency.valueOf(rs.getString("currency")),
                    PaymentStatus.valueOf(rs.getString("status")),
                    rs.getString("failure_reason"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    rs.getString("idempotency_key"),
                    rs.getLong("version")
            );
}
