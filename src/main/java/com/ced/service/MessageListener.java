package com.ced.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.ced.config.AmqpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final DataLoader dataLoader;

    public MessageListener(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @RabbitListener(queues = AmqpConfig.QUEUE_NAME)
    public void handleMessage(String message) {
        LOGGER.info("Mensagem recebida da fila: {}", message);
        dataLoader.refreshData();
    }
}
