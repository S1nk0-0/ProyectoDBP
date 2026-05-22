package com.example.proyectodbp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventarioRequestDTO {

    @NotNull
    @PositiveOrZero
    private Integer cantidadDisponible;

    @NotNull
    @PositiveOrZero
    private Integer stockMinimo;
}
