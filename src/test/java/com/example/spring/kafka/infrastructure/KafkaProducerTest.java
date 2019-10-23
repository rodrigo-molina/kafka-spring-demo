package com.example.spring.kafka.infrastructure;

import com.example.spring.kafka.infrastructure.kafka.KafkaProducer;
import com.example.spring.kafka.utils.steps.KafkaThenSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@TestPropertySource("classpath:application-test.yml")
public class KafkaProducerTest extends AbstractBaseTest implements KafkaThenSteps {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void it_should_publish_kafka_message() {
        final String key = "123";
        final String greeting = "Hello Spring Kafka Sender!";

        kafkaProducer.publish(key, greeting);

        thenThereIsAMessageInKafkaTopic(key, greeting);
    }
}
