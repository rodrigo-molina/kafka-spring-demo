package com.example.spring.kafka.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public KafkaProducer(@Value(value = "${kafka.topic}") final String topicName,
                         final KafkaTemplate<String, String> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void publish(final String key, final String value) {
        kafkaTemplate.send(topicName, key, value);
    }
}
