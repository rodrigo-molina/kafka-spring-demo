package com.example.spring.kafka.infrastructure;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.spring.kafka.utils.KafkaConsumerTestHelper;
import com.example.spring.kafka.utils.KafkaProducerTestHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractBaseTest {
    protected static final String TOPIC = "my-topic-test";
    protected static final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    @Autowired
    private KafkaConsumerTestHelper kafkaConsumerTestHelper;

    @Autowired
    private KafkaProducerTestHelper kafkaProducerTestHelper;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TOPIC);

    @Before
    public void setUpBase() {
        kafkaConsumerTestHelper.enableConsumerSupport(TOPIC, embeddedKafka);
        kafkaProducerTestHelper.enableProducerSupport(TOPIC, embeddedKafka);
    }

    @After
    public void tearDownBase() {
        kafkaProducerTestHelper.tearDown();
    }

    public ListAppender<ILoggingEvent> getListAppender() {
        return this.listAppender;
    }

    public KafkaTemplate<String, String> getKafkaTemplate() {
        return this.kafkaConsumerTestHelper.getKafkaTemplate();
    }

    public BlockingQueue<ConsumerRecord<String, String>> getRecords() {
        return this.kafkaProducerTestHelper.getRecords();
    }

}
