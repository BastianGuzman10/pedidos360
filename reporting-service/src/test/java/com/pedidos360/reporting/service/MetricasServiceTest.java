package com.pedidos360.reporting.service;

import com.pedidos360.reporting.model.PedidoKafkaEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MetricasServiceTest {
    @Test
    void registraPedidoCreado() {
        MetricasService service = new MetricasService();
        service.registrar(new PedidoKafkaEvent(UUID.randomUUID(), "PEDIDO_CREADO", 1L,
                "cliente@example.com", "PENDIENTE", new BigDecimal("15000"), Instant.now(), 1));

        assertThat(service.snapshot().pedidosCreados()).isEqualTo(1);
        assertThat(service.snapshot().ventasAcumuladas()).isEqualByComparingTo("15000");
    }
}
