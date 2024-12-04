package com.ced.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final DataLoader dataLoader;

    public MessageListener(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @RabbitListener(queues = "#{@dynamicQueueName}")
    public void handleMessage(String message) {
        LOGGER.info("Mensagem recebida da fila: {}", message);
        dataLoader.refreshData();
    }
}
