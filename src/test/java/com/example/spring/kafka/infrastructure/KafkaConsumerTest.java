package com.example.spring.kafka.infrastructure;


import ch.qos.logback.classic.Level;
import com.example.spring.kafka.infrastructure.kafka.KafkaConsumer;
import com.example.spring.kafka.utils.KafkaGivenSupport;
import com.example.spring.kafka.utils.LogSteps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
public class KafkaConsumerTest extends KafkaConsumerBase implements LogSteps, KafkaGivenSupport {

    @Before
    public void setUp() {
        givenLoggerFor(KafkaConsumer.class, getListAppender());
    }

    @Test
    public void it_should_log_async_message() {
        final String key = "key";
        final String greeting = "Hello Spring Kafka Receiver!";
        givenMessage(key, greeting);

        thenThereIsALogWith(Level.INFO, "[Kafka Consumer] Message Received [ Hello Spring Kafka Receiver! ]");
    }
}
