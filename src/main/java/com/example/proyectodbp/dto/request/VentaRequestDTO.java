package com.example.proyectodbp.dto.request;

import com.example.proyectodbp.entity.TipoDePago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VentaRequestDTO {

    private String clienteNombre;

    @NotNull
    private TipoDePago tipo;

    @Valid
    @NotEmpty
    private List<DetalleDeVentaRequestDTO> detalles;
}
