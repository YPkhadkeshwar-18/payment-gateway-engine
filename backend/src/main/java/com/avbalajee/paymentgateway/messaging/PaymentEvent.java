package com.avbalajee.paymentgateway.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private String paymentId;
    private String merchantId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String idempotencyKey;
}
