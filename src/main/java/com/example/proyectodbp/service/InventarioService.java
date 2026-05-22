package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.InventarioRequestDTO;
import com.example.proyectodbp.dto.InventarioResponseDTO;

import java.util.List;

public interface InventarioService {
    InventarioResponseDTO getByProducto(Long productoId);
    InventarioResponseDTO updateStock(Long id, InventarioRequestDTO dto);
    List<InventarioResponseDTO> getAll();
    List<InventarioResponseDTO> getLowStock();
}
