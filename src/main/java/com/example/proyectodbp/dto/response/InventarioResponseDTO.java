package com.example.proyectodbp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventarioResponseDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidadDisponible;
    private Integer stockMinimo;
}
