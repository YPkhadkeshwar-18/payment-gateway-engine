package com.avbalajee.paymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class PaymentGatewayEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentGatewayEngineApplication.class, args);
    }
}
