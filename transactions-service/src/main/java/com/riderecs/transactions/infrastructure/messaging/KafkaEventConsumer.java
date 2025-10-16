package com.riderecs.transactions.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class KafkaEventConsumer {

    private final ObjectMapper objectMapper;

    public KafkaEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public Consumer<String> processCarListingCreated() {
        return message -> {
            try {
                System.out.println("Transaction Service received event: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
