package com.example.proyectodbp.dto.response;

import com.example.proyectodbp.entity.TipoDePago;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VentaResponseDTO {

    private Long id;
    private Long usuarioId;
    private String clienteNombre;
    private TipoDePago tipo;
    private Double total;
    private LocalDateTime fecha;
    private List<DetalleDeVentaResponseDTO> detalles;
}
