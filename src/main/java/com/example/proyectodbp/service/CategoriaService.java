package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.CategoriaRequestDTO;
import com.example.proyectodbp.dto.response.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDTO create(CategoriaRequestDTO dto);
    CategoriaResponseDTO getById(Long id);
    List<CategoriaResponseDTO> getAll();
    CategoriaResponseDTO update(Long id, CategoriaRequestDTO dto);
    void delete(Long id);
}
