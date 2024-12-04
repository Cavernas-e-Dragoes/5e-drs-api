package com.ced.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitListenerConfig {

    private final AmqpProperties amqpProperties;

    public RabbitListenerConfig(AmqpProperties amqpProperties) {
        this.amqpProperties = amqpProperties;
    }

    @Bean
    public String dynamicQueueName() {
        return amqpProperties.getQueueName();
    }
}
