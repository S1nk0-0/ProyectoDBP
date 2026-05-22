package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.ProductoRequestDTO;
import com.example.proyectodbp.dto.response.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {
    ProductoResponseDTO create(ProductoRequestDTO dto);
    ProductoResponseDTO getById(Long id);
    List<ProductoResponseDTO> getAll();
    ProductoResponseDTO update(Long id, ProductoRequestDTO dto);
    void delete(Long id);
    List<ProductoResponseDTO> getByCategoria(Long categoriaId);
}
