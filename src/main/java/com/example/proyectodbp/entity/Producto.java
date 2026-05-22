package com.example.proyectodbp.entity;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Categoria categoria;
    private Inventario inventario;
}