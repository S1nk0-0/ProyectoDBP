package com.example.proyectodbp.dto.response;

import com.example.proyectodbp.entity.EstadoCompra;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CompraResponseDTO {

    private Long id;
    private Long usuarioId;
    private Double total;
    private LocalDateTime fecha;
    private EstadoCompra estado;
    private List<DetalleCompraResponseDTO> detalles;
}
