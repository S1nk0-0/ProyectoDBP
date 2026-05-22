package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.VentaRequestDTO;
import com.example.proyectodbp.dto.VentaResponseDTO;

import java.util.List;

public interface VentaService {
    VentaResponseDTO create(VentaRequestDTO dto);
    VentaResponseDTO getById(Long id);
    List<VentaResponseDTO> getAll();
    List<VentaResponseDTO> getMisVentas();
    List<VentaResponseDTO> getByUsuario(Long usuarioId);
    void delete(Long id);
}
