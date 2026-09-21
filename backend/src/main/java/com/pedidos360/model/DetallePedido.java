package com.pedidos360.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class DetallePedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull private Long productoId;
    @NotNull @Min(1) private Integer cantidad;
    @NotNull @DecimalMin("0.01") private BigDecimal precioUnitario;

    public DetallePedido() {}
    public DetallePedido(Long productoId, Integer cantidad, BigDecimal precioUnitario) { this.productoId = productoId; this.cantidad = cantidad; this.precioUnitario = precioUnitario; }
    public Long getId() { return id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
