package com.pedidos360.reporting.service;

import com.pedidos360.reporting.model.PedidoKafkaEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MetricasService {
    private final AtomicLong eventosProcesados = new AtomicLong();
    private final AtomicLong pedidosCreados = new AtomicLong();
    private final AtomicLong estadosActualizados = new AtomicLong();
    private final AtomicReference<BigDecimal> ventasAcumuladas = new AtomicReference<>(BigDecimal.ZERO);
    private final Map<String, AtomicLong> eventosPorEstado = new ConcurrentHashMap<>();
    private volatile Instant ultimaActualizacion;

    public void registrar(PedidoKafkaEvent evento) {
        eventosProcesados.incrementAndGet();
        if ("PEDIDO_CREADO".equals(evento.eventType())) {
            pedidosCreados.incrementAndGet();
            if (evento.total() != null) {
                ventasAcumuladas.updateAndGet(actual -> actual.add(evento.total()));
            }
        } else if ("PEDIDO_ESTADO_ACTUALIZADO".equals(evento.eventType())) {
            estadosActualizados.incrementAndGet();
        }
        if (evento.estado() != null) {
            eventosPorEstado.computeIfAbsent(evento.estado(), ignored -> new AtomicLong()).incrementAndGet();
        }
        ultimaActualizacion = Instant.now();
    }

    public MetricasSnapshot snapshot() {
        Map<String, Long> porEstado = new ConcurrentHashMap<>();
        eventosPorEstado.forEach((estado, cantidad) -> porEstado.put(estado, cantidad.get()));
        return new MetricasSnapshot(eventosProcesados.get(), pedidosCreados.get(),
                estadosActualizados.get(), ventasAcumuladas.get(), porEstado, ultimaActualizacion);
    }

    public record MetricasSnapshot(long eventosProcesados, long pedidosCreados,
                                   long estadosActualizados, BigDecimal ventasAcumuladas,
                                   Map<String, Long> eventosPorEstado, Instant ultimaActualizacion) {}
}
