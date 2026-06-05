package com.example.paymentservice.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PaymentIdempotencyService {
    private static final String KEY_PREFIX = "payment:idempotency:";
    private static final Duration LOCK_TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redisTemplate;

    public boolean tryLock(String idempotencyKey) {
        String redisKey = buildKey(idempotencyKey);

        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(redisKey, "PROCESSING", LOCK_TTL);

        return Boolean.TRUE.equals(result);
    }

    public void markCompleted(String idempotencyKey, String paymentId) {
        String redisKey = buildKey(idempotencyKey);

        redisTemplate.opsForValue()
                .set(redisKey, paymentId, Duration.ofHours(24));
    }

    private String buildKey(String idempotencyKey) {
        return KEY_PREFIX + idempotencyKey;
    }
}
