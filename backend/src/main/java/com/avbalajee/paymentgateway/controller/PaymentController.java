package com.avbalajee.paymentgateway.controller;

import com.avbalajee.paymentgateway.dto.CreatePaymentRequest;
import com.avbalajee.paymentgateway.dto.PaymentResponse;
import com.avbalajee.paymentgateway.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.createPayment(request));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> recentPayments() {
        return ResponseEntity.ok(paymentService.recentPayments());
    }
}
