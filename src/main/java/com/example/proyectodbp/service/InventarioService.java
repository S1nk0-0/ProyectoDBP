package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.InventarioRequestDTO;
import com.example.proyectodbp.dto.response.InventarioResponseDTO;

import java.util.List;

public interface InventarioService {
    InventarioResponseDTO getByProducto(Long productoId);
    InventarioResponseDTO updateStock(Long id, InventarioRequestDTO dto);
    List<InventarioResponseDTO> getAll();
    List<InventarioResponseDTO> getLowStock();
}
