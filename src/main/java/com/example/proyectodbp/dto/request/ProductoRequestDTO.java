package com.example.proyectodbp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequestDTO {

    @NotNull
    private Long categoriaId;

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    @Positive
    private Double precio;

    @NotNull
    @Positive
    private Double costoUnitario;
}
