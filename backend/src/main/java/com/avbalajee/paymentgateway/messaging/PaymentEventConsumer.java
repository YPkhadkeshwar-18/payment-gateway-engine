package com.avbalajee.paymentgateway.messaging;

import com.avbalajee.paymentgateway.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "${app.kafka.payment-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(PaymentEvent event) {
        log.info("Consuming payment event for paymentId={}", event.getPaymentId());
        paymentService.processPayment(event);
    }
}
