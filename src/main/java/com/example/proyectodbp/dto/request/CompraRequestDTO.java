package com.example.proyectodbp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompraRequestDTO {

    @Valid
    @NotEmpty
    private List<DetalleCompraRequestDTO> detalles;
}
