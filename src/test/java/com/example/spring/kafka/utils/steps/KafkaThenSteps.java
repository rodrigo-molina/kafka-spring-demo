package com.example.spring.kafka.utils.steps;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public interface KafkaThenSteps<K, V> {

    int POLLING_TIMEOUT_SECONDS = 2;

    BlockingQueue<ConsumerRecord<K, V>> getRecords();

    default void thenThereIsAMessageInKafkaTopic(final K key, final V value) {

        try {
            final ConsumerRecord<K, V> received = getRecords().poll(POLLING_TIMEOUT_SECONDS, SECONDS);

            assertThat(received)
                    .hasFieldOrPropertyWithValue("key", key)
                    .hasFieldOrPropertyWithValue("value", value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
