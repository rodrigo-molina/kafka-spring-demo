package com.example.spring.kafka.utils.steps;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public interface KafkaThenSteps {

    int POLLING_TIMEOUT_SECONDS = 2;

    BlockingQueue<ConsumerRecord<String, String>> getRecords();

    default void thenThereIsAMessageInKafkaTopic(final String key, final String value) {

        try {
            final ConsumerRecord<String, String> received = getRecords().poll(POLLING_TIMEOUT_SECONDS, SECONDS);

            assertThat(received)
                    .hasFieldOrPropertyWithValue("key", key)
                    .hasFieldOrPropertyWithValue("value", value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
