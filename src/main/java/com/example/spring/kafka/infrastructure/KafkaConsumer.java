package com.example.spring.kafka.infrastructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class KafkaConsumer {
    private final static Logger LOGGER = LogManager.getLogger(KafkaConsumer.class);
    private final static String LOG_FORMAT = "[Kafka Consumer] Message Received [ %s ]";

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(final String messageValue) {
        logMessage(messageValue);
    }

    private void logMessage(final String messageValue) {
        LOGGER.info(format(LOG_FORMAT, messageValue));
    }
}
