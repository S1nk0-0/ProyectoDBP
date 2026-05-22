package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.CompraRequestDTO;
import com.example.proyectodbp.dto.CompraResponseDTO;
import com.example.proyectodbp.entity.EstadoCompra;

import java.util.List;

public interface CompraService {
    CompraResponseDTO create(CompraRequestDTO dto);
    CompraResponseDTO getById(Long id);
    List<CompraResponseDTO> getAll();
    List<CompraResponseDTO> getMisCompras();
    List<CompraResponseDTO> getByUsuario(Long usuarioId);
    CompraResponseDTO updateEstado(Long id, EstadoCompra estado);
}
