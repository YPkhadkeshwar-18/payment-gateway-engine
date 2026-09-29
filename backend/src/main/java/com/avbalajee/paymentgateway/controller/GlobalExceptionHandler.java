package com.avbalajee.paymentgateway.controller;

import com.avbalajee.paymentgateway.dto.PaymentResponse;
import com.avbalajee.paymentgateway.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentService.DuplicatePaymentException.class)
    public ResponseEntity<PaymentResponse> handleDuplicate(PaymentService.DuplicatePaymentException ex) {
        return ResponseEntity.status(HttpStatus.OK).body(PaymentResponse.builder()
                .paymentId(ex.getPaymentId())
                .status(null)
                .message("Duplicate request detected. Returning existing payment reference.")
                .createdAt(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
