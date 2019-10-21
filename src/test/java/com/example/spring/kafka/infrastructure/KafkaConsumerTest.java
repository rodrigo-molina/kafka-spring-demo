package com.example.spring.kafka.infrastructure;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.spring.kafka.infrastructure.kafka.KafkaConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@TestPropertySource("classpath:application-test.yml")
public class KafkaConsumerTest extends KafkaConsumerBase {
    public static final ListAppender<ILoggingEvent> LIST_APPENDER = new ListAppender<>();

    @Before
    public void setUp() {
        givenLoggerFor(KafkaConsumer.class, LIST_APPENDER);
    }

    @Test
    public void it_should_log_async_message() {
        final String key = "key";
        final String greeting = "Hello Spring Kafka Receiver!";
        givenMessage(key, greeting);

        thenThereIsALogWith(Level.INFO, "[Kafka Consumer] Message Received [ Hello Spring Kafka Receiver! ]");
    }

    private void givenLoggerFor(final Class clazz, final ListAppender<ILoggingEvent> appender) {
        final Logger fooLogger = (Logger) LoggerFactory.getLogger(clazz);
        fooLogger.addAppender(appender);

        appender.start();
    }

    private void thenThereIsALogWith(final Level level, final String loggedMessage) {
        final List<ILoggingEvent> list = LIST_APPENDER.list;
        final Predicate<ILoggingEvent> levelsAreEqual = m -> level.equals(m.getLevel());
        final Predicate<ILoggingEvent> messagesAreEqual = m -> loggedMessage.equals(m.getMessage());

        assertThat(list).anyMatch(levelsAreEqual.and(messagesAreEqual));
    }
}
