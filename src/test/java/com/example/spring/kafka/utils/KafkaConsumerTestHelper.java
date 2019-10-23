package com.example.spring.kafka.utils;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.kafka.test.utils.ContainerTestUtils.waitForAssignment;

@Component
public class KafkaConsumerTestHelper {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaTemplate<String, String> getKafkaTemplate() {
        return kafkaTemplate;
    }

    public KafkaTemplate<String, String> enableConsumerSupport(final String topic, final EmbeddedKafkaRule embeddedKafka) {
        final Map<String, Object> senderProperties = getKafkaConfig(embeddedKafka);
        final ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory(senderProperties);

        kafkaTemplate = new KafkaTemplate(producerFactory);
        kafkaTemplate.setDefaultTopic(topic);

        waitUntilPartitionsAreAssigned(kafkaListenerEndpointRegistry, embeddedKafka);

        return kafkaTemplate;
    }

    private void waitUntilPartitionsAreAssigned(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, EmbeddedKafkaRule embeddedKafka) {
        kafkaListenerEndpointRegistry.getListenerContainers().stream()
                .forEach(messageListenerContainer -> waitForAssignment(messageListenerContainer, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic()));
    }

    private Map<String, Object> getKafkaConfig(EmbeddedKafkaRule embeddedKafka) {
        final Map<String, Object> senderProperties = KafkaTestUtils.senderProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return senderProperties;
    }
}
