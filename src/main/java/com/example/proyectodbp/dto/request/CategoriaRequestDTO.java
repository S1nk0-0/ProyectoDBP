package com.example.proyectodbp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {

    @NotBlank
    private String nombre;

    private String descripcion;
}
