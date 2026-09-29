package com.avbalajee.paymentgateway.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Value("${app.kafka.payment-topic}")
    private String topic;

    public void publish(PaymentEvent event) {
        kafkaTemplate.send(topic, event.getPaymentId(), event);
    }
}
