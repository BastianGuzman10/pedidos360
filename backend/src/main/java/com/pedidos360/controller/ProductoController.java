package com.pedidos360.controller;

import com.pedidos360.model.Producto;
import com.pedidos360.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoRepository repository;
    public ProductoController(ProductoRepository repository) { this.repository = repository; }
    @GetMapping public Iterable<Producto> listar() { return repository.findAll(); }
    @GetMapping("/{id}") public ResponseEntity<Producto> obtener(@PathVariable Long id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) { Producto creado = repository.save(producto); return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado); }
    @PutMapping("/{id}") public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto datos) { return repository.findById(id).map(actual -> { actual.setNombre(datos.getNombre()); actual.setDescripcion(datos.getDescripcion()); actual.setPrecio(datos.getPrecio()); actual.setStock(datos.getStock()); return ResponseEntity.ok(repository.save(actual)); }).orElse(ResponseEntity.notFound().build()); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id) { if (!repository.existsById(id)) return ResponseEntity.notFound().build(); repository.deleteById(id); return ResponseEntity.noContent().build(); }
}
