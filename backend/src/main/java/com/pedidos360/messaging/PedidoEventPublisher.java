package com.pedidos360.messaging;

import com.pedidos360.config.RabbitMQConfig;
import com.pedidos360.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class PedidoEventPublisher {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PedidoEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PedidoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarPedidoCreado(Pedido pedido) {

        PedidoCreadoEvent evento = new PedidoCreadoEvent(
                pedido.getId(),
                pedido.getCorreoCliente(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getFecha()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PEDIDOS_EXCHANGE,
                    RabbitMQConfig.PEDIDOS_ROUTING_KEY,
                    evento
            );

            LOGGER.info(
                    "Evento de pedido publicado en RabbitMQ. Pedido ID: {}",
                    pedido.getId()
            );

        } catch (AmqpException error) {
            LOGGER.error(
                    "No fue posible publicar el pedido {} en RabbitMQ",
                    pedido.getId(),
                    error
            );
        }
    }
}