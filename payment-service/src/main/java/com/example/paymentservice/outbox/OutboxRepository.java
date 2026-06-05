package com.example.paymentservice.outbox;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepository {
    private final JdbcTemplate jdbcTemplate;
}
