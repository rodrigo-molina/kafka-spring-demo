package com.example.spring.kafka.infrastructure;

import com.example.spring.kafka.infrastructure.kafka.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.springframework.kafka.test.utils.ContainerTestUtils.waitForAssignment;

public abstract class KafkaConsumerBase {
    private final static String CONSUMER_TOPIC = "my-topic-test";

    private KafkaTemplate<String, String> template;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, CONSUMER_TOPIC);

    @Before
    public void setUpBase() {
        final Map<String, Object> senderProperties = getKafkaConfig();
        final ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory(senderProperties);

        template = new KafkaTemplate(producerFactory);
        template.setDefaultTopic(CONSUMER_TOPIC);

        waitUntilPartitionsAreAssigned();
    }

    public void givenMessage(final String key, final String value) {
        try {
            template.sendDefault(key, value).get();
        } catch (Exception e) {
            throw new RuntimeException("Error while sending message and waiting for kafka commit", e);
        }
    }

    private void waitUntilPartitionsAreAssigned() {
        kafkaListenerEndpointRegistry.getListenerContainers().stream()
                .forEach(messageListenerContainer -> waitForAssignment(messageListenerContainer, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic()));
    }

    private Map<String, Object> getKafkaConfig() {
        final Map<String, Object> senderProperties = KafkaTestUtils.senderProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return senderProperties;
    }

}
