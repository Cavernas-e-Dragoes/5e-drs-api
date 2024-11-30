package com.ced.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    public static final String QUEUE_NAME = "refreshQueue";
    public static final String EXCHANGE_NAME = "refreshExchange";
    public static final String ROUTING_KEY = "refreshKey";

    @Bean
    public Queue refreshQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange refreshExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue refreshQueue, DirectExchange refreshExchange) {
        return BindingBuilder.bind(refreshQueue).to(refreshExchange).with(ROUTING_KEY);
    }
}
