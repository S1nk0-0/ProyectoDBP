package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.CategoriaRequestDTO;
import com.example.proyectodbp.dto.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDTO create(CategoriaRequestDTO dto);
    CategoriaResponseDTO getById(Long id);
    List<CategoriaResponseDTO> getAll();
    CategoriaResponseDTO update(Long id, CategoriaRequestDTO dto);
    void delete(Long id);
}
