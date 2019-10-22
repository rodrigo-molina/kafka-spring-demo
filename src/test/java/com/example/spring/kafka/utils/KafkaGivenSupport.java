package com.example.spring.kafka.utils;

import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaGivenSupport {
    KafkaTemplate<String, String> getKafkaTemplate();

    default void givenMessage(final String key, final String value) {
        try {
            getKafkaTemplate().sendDefault(key, value).get();
        } catch (Exception e) {
            throw new RuntimeException("Error while sending message and waiting for kafka commit", e);
        }
    }
}
