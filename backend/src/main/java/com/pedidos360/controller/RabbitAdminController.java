package com.pedidos360.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rabbit/admin")
public class RabbitAdminController {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminController(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostMapping("/colas")
    public ResponseEntity<Map<String, String>> crearCola(
            @Valid @RequestBody ColaRequest request
    ) {
        Queue cola = new Queue(request.nombre(), true, false, false);
        amqpAdmin.declareQueue(cola);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Cola creada correctamente",
                "cola", request.nombre()
        ));
    }

    @DeleteMapping("/colas/{nombre}")
    public ResponseEntity<Map<String, String>> eliminarCola(
            @PathVariable String nombre
    ) {
        amqpAdmin.deleteQueue(nombre);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Cola eliminada correctamente",
                "cola", nombre
        ));
    }

    @PostMapping("/exchanges")
    public ResponseEntity<Map<String, String>> crearExchange(
            @Valid @RequestBody ExchangeRequest request
    ) {
        Exchange exchange = switch (request.tipo().toLowerCase()) {
            case "topic" ->
                    new TopicExchange(request.nombre(), true, false);
            case "fanout" ->
                    new FanoutExchange(request.nombre(), true, false);
            default ->
                    new DirectExchange(request.nombre(), true, false);
        };

        amqpAdmin.declareExchange(exchange);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Exchange creado correctamente",
                "exchange", request.nombre(),
                "tipo", request.tipo()
        ));
    }

    @DeleteMapping("/exchanges/{nombre}")
    public ResponseEntity<Map<String, String>> eliminarExchange(
            @PathVariable String nombre
    ) {
        amqpAdmin.deleteExchange(nombre);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Exchange eliminado correctamente",
                "exchange", nombre
        ));
    }

    @PostMapping("/bindings")
    public ResponseEntity<Map<String, String>> crearBinding(
            @Valid @RequestBody BindingRequest request
    ) {
        Binding binding = crearBindingDesdeRequest(request);
        amqpAdmin.declareBinding(binding);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Binding creado correctamente",
                "cola", request.cola(),
                "exchange", request.exchange(),
                "routingKey", request.routingKey()
        ));
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<Map<String, String>> eliminarBinding(
            @Valid @RequestBody BindingRequest request
    ) {
        Binding binding = crearBindingDesdeRequest(request);
        amqpAdmin.removeBinding(binding);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Binding eliminado correctamente",
                "cola", request.cola(),
                "exchange", request.exchange()
        ));
    }

    private Binding crearBindingDesdeRequest(BindingRequest request) {
        return new Binding(
                request.cola(),
                Binding.DestinationType.QUEUE,
                request.exchange(),
                request.routingKey(),
                null
        );
    }

    public record ColaRequest(@NotBlank String nombre) {
    }

    public record ExchangeRequest(
            @NotBlank String nombre,
            @NotBlank String tipo
    ) {
    }

    public record BindingRequest(
            @NotBlank String cola,
            @NotBlank String exchange,
            @NotBlank String routingKey
    ) {
    }
}