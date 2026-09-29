package com.avbalajee.paymentgateway.dto;

import com.avbalajee.paymentgateway.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private String paymentId;
    private String merchantId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private PaymentStatus status;
    private String message;
    private LocalDateTime createdAt;
}
