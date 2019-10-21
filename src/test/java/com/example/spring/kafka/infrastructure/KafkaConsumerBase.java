package com.example.spring.kafka.infrastructure;

import com.example.spring.kafka.infrastructure.kafka.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class KafkaConsumerBase {

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
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getEmbeddedKafka().getBrokersAsString());
    }

    @Before
    public void setUp() {
        final Map<String, Object> senderProperties = KafkaTestUtils
                .senderProps(embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory(senderProperties);

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

    public void givenMessage(final String key, final String value) {
        try {
            template.sendDefault(key, value).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
