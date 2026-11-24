package com.pedidos360.messaging;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PedidoKafkaEvent(
        UUID eventId,
        String eventType,
        Long pedidoId,
        String correoCliente,
        String estado,
        BigDecimal total,
        Instant occurredAt,
        int schemaVersion
) {
}
