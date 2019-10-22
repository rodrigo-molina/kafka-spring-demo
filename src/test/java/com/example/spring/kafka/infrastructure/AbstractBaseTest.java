package com.example.spring.kafka.infrastructure;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.spring.kafka.utils.KafkaConsumerTestHelper;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

public abstract class AbstractBaseTest {

    protected static final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private final static String CONSUMER_TOPIC = "my-topic-test";

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumerTestHelper kafkaConsumerTestHelper;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, CONSUMER_TOPIC);

    @Before
    public void setUpBase() {
        kafkaTemplate = kafkaConsumerTestHelper.enableConsumerSupport(CONSUMER_TOPIC, embeddedKafka);
    }


    public ListAppender<ILoggingEvent> getListAppender() {
        return this.listAppender;
    }

    public KafkaTemplate<String, String> getKafkaTemplate() {
        return kafkaTemplate;
    }


}
