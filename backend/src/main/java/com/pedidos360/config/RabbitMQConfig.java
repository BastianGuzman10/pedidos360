package com.pedidos360.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PEDIDOS_EXCHANGE = "pedidos.exchange";
    public static final String PEDIDOS_QUEUE = "pedidos.procesamiento";
    public static final String PEDIDOS_ROUTING_KEY = "pedidos.creado";

    public static final String DEAD_LETTER_EXCHANGE = "pedidos.dlx";
    public static final String DEAD_LETTER_QUEUE = "pedidos.dlq";
    public static final String DEAD_LETTER_ROUTING_KEY = "pedidos.error";

    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(PEDIDOS_EXCHANGE, true, false);
    }

    @Bean
    public Queue pedidosQueue() {
        return QueueBuilder
                .durable(PEDIDOS_QUEUE)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding pedidosBinding() {
        return BindingBuilder
                .bind(pedidosQueue())
                .to(pedidosExchange())
                .with(PEDIDOS_ROUTING_KEY);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}