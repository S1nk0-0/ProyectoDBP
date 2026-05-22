package com.example.proyectodbp.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Venta {
    private Long id;
    private Usuario usuario;           // dueño del negocio que registra
    private String clienteNombre;      // opcional, venta anónima permitida
    private List<DetalleDeVenta> detalles;
    private TipoDePago tipo;
    private Double total;
    private LocalDateTime fecha;
}