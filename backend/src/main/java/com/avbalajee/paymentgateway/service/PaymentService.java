package com.avbalajee.paymentgateway.service;

import com.avbalajee.paymentgateway.dto.CreatePaymentRequest;
import com.avbalajee.paymentgateway.dto.PaymentResponse;
import com.avbalajee.paymentgateway.entity.PaymentStatus;
import com.avbalajee.paymentgateway.entity.PaymentTransaction;
import com.avbalajee.paymentgateway.messaging.PaymentEvent;
import com.avbalajee.paymentgateway.messaging.PaymentEventPublisher;
import com.avbalajee.paymentgateway.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentTransactionRepository repository;
    private final PaymentEventPublisher publisher;
    private final StringRedisTemplate redisTemplate;
    private final Random random = new Random();

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        String redisKey = "payment:idempotency:" + request.getIdempotencyKey();

        String existingPaymentId = redisTemplate.opsForValue().get(redisKey);
        if (existingPaymentId != null) {
            return repository.findById(existingPaymentId)
                    .map(this::toResponse)
                    .orElseThrow();
        }

        repository.findByIdempotencyKey(request.getIdempotencyKey())
                .ifPresent(tx -> { throw new DuplicatePaymentException(tx.getId()); });

        PaymentTransaction payment = PaymentTransaction.builder()
                .idempotencyKey(request.getIdempotencyKey())
                .merchantId(request.getMerchantId())
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .currency(request.getCurrency().toUpperCase())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .retryCount(0)
                .build();

        try {
            PaymentTransaction saved = repository.save(payment);
            redisTemplate.opsForValue().set(redisKey, saved.getId(), Duration.ofHours(12));
            publisher.publish(PaymentEvent.builder()
                    .paymentId(saved.getId())
                    .merchantId(saved.getMerchantId())
                    .customerId(saved.getCustomerId())
                    .amount(saved.getAmount())
                    .currency(saved.getCurrency())
                    .paymentMethod(saved.getPaymentMethod())
                    .idempotencyKey(saved.getIdempotencyKey())
                    .build());
            return toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            PaymentTransaction existing = repository.findByIdempotencyKey(request.getIdempotencyKey()).orElseThrow();
            return toResponse(existing);
        }
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentId) {
        return repository.findById(paymentId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> recentPayments() {
        return repository.findTop20ByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    @Transactional
    public void processPayment(PaymentEvent event) {
        PaymentTransaction tx = repository.findById(event.getPaymentId()).orElseThrow();
        tx.setStatus(PaymentStatus.PROCESSING);
        repository.save(tx);

        boolean transientFailure = random.nextInt(10) < 3;
        if (transientFailure) {
            tx.setRetryCount(tx.getRetryCount() + 1);
            tx.setStatus(PaymentStatus.RETRYING);
            tx.setFailureReason("Transient processor timeout");
            repository.save(tx);
            throw new RuntimeException("Payment processor timeout");
        }

        tx.setStatus(PaymentStatus.SUCCESS);
        tx.setFailureReason(null);
        repository.save(tx);
    }

    @Recover
    @Transactional
    public void recover(RuntimeException ex, PaymentEvent event) {
        PaymentTransaction tx = repository.findById(event.getPaymentId()).orElseThrow();
        tx.setStatus(PaymentStatus.FAILED);
        tx.setFailureReason(ex.getMessage());
        repository.save(tx);
    }

    private PaymentResponse toResponse(PaymentTransaction tx) {
        return PaymentResponse.builder()
                .paymentId(tx.getId())
                .merchantId(tx.getMerchantId())
                .customerId(tx.getCustomerId())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .paymentMethod(tx.getPaymentMethod())
                .status(tx.getStatus())
                .message(tx.getFailureReason() == null ? "Payment accepted" : tx.getFailureReason())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    public static class DuplicatePaymentException extends RuntimeException {
        private final String paymentId;

        public DuplicatePaymentException(String paymentId) {
            super("Duplicate payment request");
            this.paymentId = paymentId;
        }

        public String getPaymentId() {
            return paymentId;
        }
    }
}
