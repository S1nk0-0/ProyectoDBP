package com.example.proyectodbp.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Categoria {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<Producto> productos;
}
