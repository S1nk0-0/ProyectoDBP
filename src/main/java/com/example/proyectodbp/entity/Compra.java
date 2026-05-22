package com.example.proyectodbp.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class Compra {
    private Long id;
    private Usuario usuario;
    private Map<Producto, Integer> productos; // Producto -> cantidad
    private BigDecimal total;
    private LocalDateTime fecha;
    private EstadoCompra estado;
}
