package com.example.spring.kafka.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public interface LogSteps {
    ListAppender<ILoggingEvent> getListAppender();

    default void givenLoggerFor(final Class clazz, final ListAppender<ILoggingEvent> appender) {
        final Logger fooLogger = (Logger) LoggerFactory.getLogger(clazz);
        fooLogger.addAppender(appender);

        appender.start();
    }

    default void thenThereIsALogWith(final Level level, final String loggedMessage) {
        final List<ILoggingEvent> list = getListAppender().list;
        final Predicate<ILoggingEvent> levelsAreEqual = m -> level.equals(m.getLevel());
        final Predicate<ILoggingEvent> messagesAreEqual = m -> loggedMessage.equals(m.getMessage());

        assertThat(list).anyMatch(levelsAreEqual.and(messagesAreEqual));
    }
}
