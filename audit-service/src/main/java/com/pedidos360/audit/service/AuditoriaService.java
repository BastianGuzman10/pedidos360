package com.pedidos360.audit.service;

import com.pedidos360.audit.model.PedidoKafkaEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.List;

@Service
public class AuditoriaService {
    private static final int MAX_EVENTOS = 100;
    private final ArrayDeque<PedidoKafkaEvent> eventos = new ArrayDeque<>();

    public synchronized void registrar(PedidoKafkaEvent evento) {
        eventos.addFirst(evento);
        while (eventos.size() > MAX_EVENTOS) {
            eventos.removeLast();
        }
    }

    public synchronized List<PedidoKafkaEvent> recientes() {
        return List.copyOf(eventos);
    }
}
