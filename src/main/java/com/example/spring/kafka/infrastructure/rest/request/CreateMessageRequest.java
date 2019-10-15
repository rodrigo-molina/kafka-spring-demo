package com.example.spring.kafka.infrastructure.rest.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMessageRequest {

    private final String key;
    private final String value;

    @JsonCreator
    public CreateMessageRequest(@JsonProperty("key") final String key, @JsonProperty("value") final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
