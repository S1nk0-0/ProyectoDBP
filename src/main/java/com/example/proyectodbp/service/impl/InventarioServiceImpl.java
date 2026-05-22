package com.example.proyectodbp.service.impl;

import com.example.proyectodbp.dto.request.InventarioRequestDTO;
import com.example.proyectodbp.dto.response.InventarioResponseDTO;
import com.example.proyectodbp.entity.Inventario;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.InventarioRepository;
import com.example.proyectodbp.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public InventarioResponseDTO getByProducto(Long productoId) {
        return toResponseDTO(inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventario no encontrado para producto id: " + productoId)));
    }

    @Override
    @Transactional
    public InventarioResponseDTO updateStock(Long id, InventarioRequestDTO dto) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id: " + id));
        inventario.setCantidadDisponible(dto.getCantidadDisponible());
        inventario.setStockMinimo(dto.getStockMinimo());
        return toResponseDTO(inventario);
    }

    @Override
    public List<InventarioResponseDTO> getAll() {
        return inventarioRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<InventarioResponseDTO> getLowStock() {
        return inventarioRepository.findAll().stream()
                .filter(i -> i.getCantidadDisponible() <= i.getStockMinimo())
                .map(this::toResponseDTO)
                .toList();
    }

    private InventarioResponseDTO toResponseDTO(Inventario inventario) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setId(inventario.getId());
        dto.setProductoId(inventario.getProducto().getId());
        dto.setProductoNombre(inventario.getProducto().getNombre());
        dto.setCantidadDisponible(inventario.getCantidadDisponible());
        dto.setStockMinimo(inventario.getStockMinimo());
        return dto;
    }
}
