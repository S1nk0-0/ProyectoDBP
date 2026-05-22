package com.example.proyectodbp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleCompraRequestDTO {

    @NotNull
    private Long productoId;

    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    @Positive
    private Double precioUnitario;
}
