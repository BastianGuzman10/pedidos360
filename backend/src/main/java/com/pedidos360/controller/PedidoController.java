package com.pedidos360.controller;

import com.pedidos360.messaging.PedidoEventPublisher;
import com.pedidos360.model.Pedido;
import com.pedidos360.repository.PedidoRepository;
import com.pedidos360.repository.ProductoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository repository;
    private final ProductoRepository productoRepository;
    private final PedidoEventPublisher pedidoEventPublisher;

    public PedidoController(
            PedidoRepository repository,
            ProductoRepository productoRepository,
            PedidoEventPublisher pedidoEventPublisher
    ) {
        this.repository = repository;
        this.productoRepository = productoRepository;
        this.pedidoEventPublisher = pedidoEventPublisher;
    }

    @GetMapping
    public Iterable<Pedido> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtener(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> crear(@Valid @RequestBody Pedido pedido) {

        if (pedido.getDetalles().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El pedido debe incluir al menos un producto"
            );
        }

        BigDecimal total = BigDecimal.ZERO;

        for (var detalle : pedido.getDetalles()) {

            var producto = productoRepository
                    .findById(detalle.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Producto inexistente: " + detalle.getProductoId()
                    ));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente para " + producto.getNombre()
                );
            }

            detalle.setPrecioUnitario(producto.getPrecio());
            producto.setStock(
                    producto.getStock() - detalle.getCantidad()
            );

            total = total.add(
                    producto.getPrecio().multiply(
                            BigDecimal.valueOf(detalle.getCantidad())
                    )
            );
        }

        pedido.setTotal(total);

        Pedido creado = repository.save(pedido);

        pedidoEventPublisher.publicarPedidoCreado(creado);

        return ResponseEntity
                .created(URI.create("/api/pedidos/" + creado.getId()))
                .body(creado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest request
    ) {
        return repository.findById(id)
                .map(pedido -> {
                    pedido.setEstado(request.estado());
                    return ResponseEntity.ok(repository.save(pedido));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record EstadoRequest(@NotBlank String estado) {
    }
}