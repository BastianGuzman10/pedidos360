package com.pedidos360.audit.controller;

import com.pedidos360.audit.model.PedidoKafkaEvent;
import com.pedidos360.audit.service.AuditoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {
    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/eventos")
    public List<PedidoKafkaEvent> eventosRecientes() {
        return auditoriaService.recientes();
    }
}
