package com.example.spring.kafka.utils.steps;

import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaGivenSteps<K, V> {
    KafkaTemplate<K, V> getKafkaTemplate();

    default void givenMessage(final K key, final V value) {
        try {
            getKafkaTemplate().sendDefault(key, value).get();
        } catch (Exception e) {
            throw new RuntimeException("Error while sending message and waiting for kafka commit", e);
        }
    }
}
