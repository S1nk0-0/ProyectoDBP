package com.example.proyectodbp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Double costoUnitario;
    private Long categoriaId;
    private String categoriaNombre;
}
