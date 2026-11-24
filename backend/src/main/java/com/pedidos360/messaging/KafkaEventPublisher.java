package com.pedidos360.messaging;

import com.pedidos360.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class KafkaEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaTemplate<String, PedidoKafkaEvent> kafkaTemplate;
    private final String pedidosTopic;
    private final String auditoriaTopic;

    public KafkaEventPublisher(
            KafkaTemplate<String, PedidoKafkaEvent> kafkaTemplate,
            @Value("${app.kafka.pedidos-topic}") String pedidosTopic,
            @Value("${app.kafka.auditoria-topic}") String auditoriaTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.pedidosTopic = pedidosTopic;
        this.auditoriaTopic = auditoriaTopic;
    }

    public void publicar(Pedido pedido, String eventType) {
        PedidoKafkaEvent evento = new PedidoKafkaEvent(
                UUID.randomUUID(),
                eventType,
                pedido.getId(),
                pedido.getCorreoCliente(),
                pedido.getEstado(),
                pedido.getTotal(),
                Instant.now(),
                1
        );

        publicarEnTopico(pedidosTopic, evento);
        publicarEnTopico(auditoriaTopic, evento);
    }

    private void publicarEnTopico(String topic, PedidoKafkaEvent evento) {
        try {
            kafkaTemplate.send(topic, evento.pedidoId().toString(), evento)
                    .whenComplete((resultado, error) -> {
                        if (error != null) {
                            LOGGER.error("Error publicando evento {} en {} para pedido {}",
                                    evento.eventId(), topic, evento.pedidoId(), error);
                            return;
                        }
                        LOGGER.info("Evento {} publicado en {} partición {} offset {}",
                                evento.eventId(), topic,
                                resultado.getRecordMetadata().partition(),
                                resultado.getRecordMetadata().offset());
                    });
        } catch (RuntimeException error) {
            LOGGER.error("Kafka no disponible; el pedido {} permanece confirmado y el evento {} queda en log",
                    evento.pedidoId(), evento.eventId(), error);
        }
    }
}
