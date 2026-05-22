package com.example.proyectodbp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleCompraResponseDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
