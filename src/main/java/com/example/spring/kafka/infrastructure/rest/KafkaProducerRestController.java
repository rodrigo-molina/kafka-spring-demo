package com.example.spring.kafka.infrastructure.rest;

import com.example.spring.kafka.infrastructure.kafka.KafkaProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring.kafka.infrastructure.rest.request.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
public class KafkaProducerRestController {

    private final KafkaProducer kafkaProducer;

    public KafkaProducerRestController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }


    @GetMapping("/messages")
    public void getMessages() {
        throw new NotImplementedException();
    }


    @PostMapping("/messages")
    public void createMessage(@RequestBody final CreateMessageRequest message) {
        kafkaProducer.publish(message.getKey(), message.getValue());
    }
}
