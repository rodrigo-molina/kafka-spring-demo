package com.example.spring.kafka.infrastructure;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
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

    protected static final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private final static String CONSUMER_TOPIC = "my-topic-test";

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, CONSUMER_TOPIC);

    @Before
    public void setUpBase() {
        enableConsumerSupport();
    }


    public ListAppender<ILoggingEvent> getListAppender() {
        return this.listAppender;
    }

    public KafkaTemplate<String, String> getKafkaTemplate() {
        return kafkaTemplate;
    }

    private void enableConsumerSupport() {
        final Map<String, Object> senderProperties = getKafkaConfig();
        final ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory(senderProperties);

        kafkaTemplate = new KafkaTemplate(producerFactory);
        kafkaTemplate.setDefaultTopic(CONSUMER_TOPIC);

        waitUntilPartitionsAreAssigned();
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
