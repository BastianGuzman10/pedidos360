package com.pedidos360.reporting.consumer;

import com.pedidos360.reporting.model.PedidoKafkaEvent;
import com.pedidos360.reporting.service.MetricasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ReporteEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReporteEventConsumer.class);
    private final MetricasService metricasService;

    public ReporteEventConsumer(MetricasService metricasService) {
        this.metricasService = metricasService;
    }

    @KafkaListener(topics = "${app.kafka.pedidos-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumir(PedidoKafkaEvent evento,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) int particion,
                         @Header(KafkaHeaders.OFFSET) long offset) {
        if (!evento.valido()) {
            throw new IllegalArgumentException("Payload Kafka inválido");
        }
        metricasService.registrar(evento);
        LOGGER.info("Métrica actualizada para evento {} (partición {}, offset {})",
                evento.eventId(), particion, offset);
    }
}
