package com.example.spring.kafka.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {

        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder() {
            @Override
            public void configure(final ObjectMapper om) {
                super.configure(om);
                // make private fields of class visible to Jackson
                om.setVisibility(
                        om.getSerializationConfig()
                                .getDefaultVisibilityChecker()
                                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
            }
        };

        jackson2ObjectMapperBuilder
                .featuresToEnable(
                        MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS,
                        SerializationFeature.WRITE_ENUMS_USING_TO_STRING
                )
                .featuresToDisable(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        SerializationFeature.FAIL_ON_EMPTY_BEANS
                )
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                // Avoid having to annotate the class
                // Requires Java 8, pass -parameters to javac
                // and jackson-module-parameter-names as a dependency
                .modules(new Jdk8Module(), new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        return jackson2ObjectMapperBuilder.build();
    }
}
