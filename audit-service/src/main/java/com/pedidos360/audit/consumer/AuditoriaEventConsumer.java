package com.pedidos360.audit.consumer;

import com.pedidos360.audit.model.PedidoKafkaEvent;
import com.pedidos360.audit.service.AuditoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditoriaEventConsumer.class);
    private final AuditoriaService auditoriaService;

    public AuditoriaEventConsumer(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @KafkaListener(topics = "${app.kafka.auditoria-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumir(PedidoKafkaEvent evento) {
        if (!evento.valido()) {
            throw new IllegalArgumentException("Evento de auditoría inválido");
        }
        auditoriaService.registrar(evento);
        LOGGER.info("AUDITORIA eventId={} tipo={} pedidoId={} estado={} fecha={}",
                evento.eventId(), evento.eventType(), evento.pedidoId(), evento.estado(), evento.occurredAt());
    }
}
