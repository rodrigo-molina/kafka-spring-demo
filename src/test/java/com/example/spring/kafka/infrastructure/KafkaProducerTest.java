package com.example.spring.kafka.infrastructure;

import com.example.spring.kafka.infrastructure.kafka.KafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class KafkaProducerTest {

    private static String SENDER_TOPIC = "my-topic-test";

    @Autowired
    private KafkaProducer kafkaProducer;

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, SENDER_TOPIC);

    @Before
    public void setUp() {
        final Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString(), "sender", "false");
        consumerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory(consumerProperties);

        final ContainerProperties containerProperties = new ContainerProperties(SENDER_TOPIC);

        records = new LinkedBlockingQueue<>();

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> records.add(record));
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        container.stop();
    }

    @Test
    public void it_should_publish_kafka_message() throws InterruptedException {
        final String key = "123";
        final String greeting = "Hello Spring Kafka Sender!";
        kafkaProducer.publish(key, greeting);

        final ConsumerRecord<String, String> received = records.poll(2, TimeUnit.SECONDS);

        assertThat(received).hasFieldOrPropertyWithValue("key", key);
        assertThat(received).hasFieldOrPropertyWithValue("value", greeting);

    }
}
