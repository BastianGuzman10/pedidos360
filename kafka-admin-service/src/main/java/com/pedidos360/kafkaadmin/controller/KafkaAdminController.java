package com.pedidos360.kafkaadmin.controller;

import com.pedidos360.kafkaadmin.model.TopicRequest;
import com.pedidos360.kafkaadmin.service.KafkaManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
public class KafkaAdminController {
    private final KafkaManagementService service;

    public KafkaAdminController(KafkaManagementService service) {
        this.service = service;
    }

    @PostMapping("/topics")
    public ResponseEntity<Void> crear(@Valid @RequestBody TopicRequest request) throws Exception {
        service.crear(request);
        return ResponseEntity.created(URI.create("/api/kafka/topics/" + request.nombre())).build();
    }

    @GetMapping("/topics")
    public List<KafkaManagementService.TopicInfo> listar() throws Exception {
        return service.listar();
    }

    @GetMapping("/topics/{nombre}/config")
    public Map<String, String> configuracion(@PathVariable String nombre) throws Exception {
        return service.configuracion(nombre);
    }

    @PutMapping("/topics/{nombre}")
    public ResponseEntity<Void> actualizar(@PathVariable String nombre,
            @Valid @RequestBody TopicRequest request) throws Exception {
        service.actualizar(nombre, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/topics/{nombre}")
    public ResponseEntity<Void> eliminar(@PathVariable String nombre) throws Exception {
        service.eliminar(nombre);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consumer-groups")
    public List<KafkaManagementService.ConsumerGroupInfo> consumerGroups() throws Exception {
        return service.consumerGroups();
    }
}
