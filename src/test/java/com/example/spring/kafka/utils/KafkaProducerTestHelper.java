package com.example.spring.kafka.utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class KafkaProducerTestHelper {

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    public void enableProducerSupport(final String topic, final EmbeddedKafkaRule embeddedKafka) {
        final Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString(), "sender", "false");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        final DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory(consumerProperties);

        final ContainerProperties containerProperties = new ContainerProperties(topic);

        records = new LinkedBlockingQueue<>();

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> records.add(record));
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    public void tearDown() {
        container.stop();
    }

    public BlockingQueue<ConsumerRecord<String, String>> getRecords() {
        return records;
    }
}
