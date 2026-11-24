package com.pedidos360.audit.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PedidoKafkaEvent(UUID eventId, String eventType, Long pedidoId,
        String correoCliente, String estado, BigDecimal total, Instant occurredAt,
        int schemaVersion) {
    public boolean valido() {
        return eventId != null && eventType != null && !eventType.isBlank()
                && pedidoId != null && occurredAt != null && schemaVersion > 0;
    }
}
