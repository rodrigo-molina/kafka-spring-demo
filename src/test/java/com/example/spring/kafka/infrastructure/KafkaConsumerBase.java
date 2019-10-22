package com.example.spring.kafka.infrastructure;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

import static com.example.spring.kafka.utils.KafkaConsumerTestHelper.enableConsumerSupport;

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
        kafkaTemplate = enableConsumerSupport(CONSUMER_TOPIC, kafkaListenerEndpointRegistry, embeddedKafka);
    }


    public ListAppender<ILoggingEvent> getListAppender() {
        return this.listAppender;
    }

    public KafkaTemplate<String, String> getKafkaTemplate() {
        return kafkaTemplate;
    }



}
