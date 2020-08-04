package com.nilin.springboot.service;

import com.nilin.springboot.DTO.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducerService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducerService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${fanout.exchange}")
    private String EXCHANGE;

    @Value("${routing_key.name}")
    private String ROUTING_KEY;

    public void send(MessageDto messageDto) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, messageDto);
    }

//    public void send(String msg) {
//        log.info("Sending message...");
//        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, msg);
//    }

}