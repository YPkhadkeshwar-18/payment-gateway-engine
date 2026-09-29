package com.avbalajee.paymentgateway.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    @NotBlank
    private String idempotencyKey;

    @NotBlank
    private String merchantId;

    @NotBlank
    private String customerId;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String paymentMethod;
}
