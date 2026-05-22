package com.example.proyectodbp.service.impl;

import com.example.proyectodbp.dto.CategoriaRequestDTO;
import com.example.proyectodbp.dto.CategoriaResponseDTO;
import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.CategoriaRepository;
import com.example.proyectodbp.service.CategoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public CategoriaResponseDTO create(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponseDTO getById(Long id) {
        return toResponseDTO(categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id)));
    }

    @Override
    public List<CategoriaResponseDTO> getAll() {
        return categoriaRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public CategoriaResponseDTO update(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return toResponseDTO(categoria);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
