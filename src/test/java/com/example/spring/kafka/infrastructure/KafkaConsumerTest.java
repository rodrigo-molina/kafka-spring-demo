package com.example.spring.kafka.infrastructure;

import com.example.spring.kafka.infrastructure.kafka.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@TestPropertySource("classpath:application-test.yml")
public class KafkaConsumerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerTest.class);

    @Value(value = "${kafka.topic}")
    private static String CONSUMER_TOPIC = "my-topic-test";

    @Autowired
    private KafkaConsumer kafkaConsumer;

    private KafkaTemplate<String, String> template;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, CONSUMER_TOPIC);

    @BeforeClass
    public static void setup() {
        System.setProperty("spring.kafka.bootstrap-servers",
                embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }


    @Before
    public void setUp() {

//        KafkaWindowsUtils.deleteTemporalFolderIfWindows("kafka");
        // set up the Kafka producer properties
        final Map<String, Object> senderProperties = KafkaTestUtils
                .senderProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // create a Kafka producer factory
        final ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory(senderProperties);

        // create a Kafka template
        template = new KafkaTemplate(producerFactory);
        // set the default topic to send to
        template.setDefaultTopic(CONSUMER_TOPIC);

        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
        }
    }

    @Test
    public void asdddddddddddddddddddddddddddddddddddddddddd() throws InterruptedException, ExecutionException {
        final String key = "key";
        final String greeting = "Hello Spring Kafka Receiver!";
        template.sendDefault(key, greeting).get();
        LOGGER.info("Done");
    }
}
