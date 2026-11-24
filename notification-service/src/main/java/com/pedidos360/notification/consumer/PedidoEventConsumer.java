package com.pedidos360.notification.consumer;

import com.pedidos360.messaging.PedidoCreadoEvent;
import com.pedidos360.notification.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoEventConsumer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PedidoEventConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.PEDIDOS_QUEUE)
    public void procesarPedido(PedidoCreadoEvent evento) {

        LOGGER.info(
                "Pedido recibido desde RabbitMQ: ID={}, correo={}, estado={}, total={}",
                evento.pedidoId(),
                evento.correoCliente(),
                evento.estado(),
                evento.total()
        );

        /*
         * Error controlado para demostrar la Dead Letter Queue.
         * Si el correo comienza con "error", el mensaje será rechazado
         * y RabbitMQ lo enviará a pedidos.dlq.
         */
        if (evento.correoCliente() != null
                && evento.correoCliente().toLowerCase().startsWith("error")) {

            LOGGER.error(
                    "Error controlado procesando el pedido {}. Se enviará a la DLQ.",
                    evento.pedidoId()
            );

            throw new IllegalStateException(
                    "Error simulado para comprobar la Dead Letter Queue"
            );
        }

        LOGGER.info(
                "Pedido {} procesado correctamente para {}",
                evento.pedidoId(),
                evento.correoCliente()
        );
    }
}