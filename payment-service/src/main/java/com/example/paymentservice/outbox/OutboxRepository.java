package com.example.paymentservice.outbox;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepository {
    private final JdbcTemplate jdbcTemplate;
}
