package com.example.proyectodbp.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Inventario {
    private Long id;
    private Producto producto;
    private int cantidadDisponible;
    private int cantidadMinima;
}