package com.pedidos360.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String nombre;
    private String descripcion;
    @NotNull @DecimalMin("0.01") private BigDecimal precio;
    @NotNull @Min(0) private Integer stock;

    public Producto() {}
    public Producto(String nombre, String descripcion, BigDecimal precio, Integer stock) { this.nombre = nombre; this.descripcion = descripcion; this.precio = precio; this.stock = stock; }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
