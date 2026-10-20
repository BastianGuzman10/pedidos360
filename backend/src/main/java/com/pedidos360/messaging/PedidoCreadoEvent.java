package com.pedidos360.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record PedidoCreadoEvent(
        Long pedidoId,
        String correoCliente,
        String estado,
        BigDecimal total,
        Instant fechaCreacion
) {
}