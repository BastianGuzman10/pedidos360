package com.pedidos360.audit.service;

import com.pedidos360.audit.model.PedidoKafkaEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuditoriaServiceTest {
    @Test
    void conservaEventoMasReciente() {
        AuditoriaService service = new AuditoriaService();
        PedidoKafkaEvent evento = new PedidoKafkaEvent(UUID.randomUUID(), "PEDIDO_CREADO", 1L,
                "cliente@example.com", "PENDIENTE", BigDecimal.TEN, Instant.now(), 1);
        service.registrar(evento);
        assertThat(service.recientes()).containsExactly(evento);
    }
}
