package com.example.proyectodbp.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleDeVenta {
    private Long id;
    private Venta venta;
    private Producto producto;
    private int cantidad;
    private Double precioUnitario;
    private Double subtotal;       // cantidad * precioUnitario
}
