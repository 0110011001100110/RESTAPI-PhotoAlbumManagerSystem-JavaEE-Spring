package com.nilin.springboot.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.nilin.springboot.service.RabbitMQConsumerService;

@Configuration
public class RabbitMQConfig {

   // private static final String LISTENER_METHOD = "receiveAndProcessMessage";

    @Value("${queue.name}")
    private String QUEUE;

    @Value("${fanout.exchange}")
    private String EXCHANGE;

    @Value("${routing_key.name}")
    private String ROUTING_KEY;

    @Bean
    Queue queue() {
        return new Queue(QUEUE, true);
    }
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    MessageListenerAdapter listenerAdapter(RabbitMQConsumerService listener) {
//        return new MessageListenerAdapter(listener, LISTENER_METHOD);
//    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory /*MessageListenerAdapter listenerAdapter*/) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //container.setMessageConverter(jsonMessageConverter());       // container.setQueueNames(QUEUE);
        //container.setMessageListener(listenerAdapter);

        return container;

    }



}
