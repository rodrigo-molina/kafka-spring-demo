package com.example.spring.kafka.infrastructure;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.spring.kafka.infrastructure.kafka.KafkaConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@TestPropertySource("classpath:application-test.yml")
public class KafkaConsumerTest extends KafkaConsumerBase {


    public static final ListAppender<ILoggingEvent> LIST_APPENDER = new ListAppender<>();

    @Before
    public void setUp() {
        final Logger fooLogger = (Logger) LoggerFactory.getLogger(KafkaConsumer.class);
        fooLogger.addAppender(LIST_APPENDER);

        LIST_APPENDER.start();
    }

    @Test
    public void it_should_log_async_message() {
        final String key = "key";
        final String greeting = "Hello Spring Kafka Receiver!";
        givenMessage(key, greeting);

        List<ILoggingEvent> list = LIST_APPENDER.list;
        Level level = Level.INFO;
        assertThat(list).allMatch(message -> "[INFO] [Kafka Consumer] Message Received [ Hello Spring Kafka Receiver! ]".equals(message.getMessage()) && level.equals(message.getMessage()));
    }
}
