package com.pedidos360.reporting.controller;

import com.pedidos360.reporting.service.MetricasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class MetricasController {
    private final MetricasService metricasService;

    public MetricasController(MetricasService metricasService) {
        this.metricasService = metricasService;
    }

    @GetMapping("/metricas")
    public MetricasService.MetricasSnapshot obtenerMetricas() {
        return metricasService.snapshot();
    }
}
