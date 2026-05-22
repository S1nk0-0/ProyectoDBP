package com.example.proyectodbp.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private List<Compra> compras;
    private Inventario inventario;
}