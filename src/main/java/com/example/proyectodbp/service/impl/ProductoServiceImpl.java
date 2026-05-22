package com.example.proyectodbp.service.impl;

import com.example.proyectodbp.dto.request.ProductoRequestDTO;
import com.example.proyectodbp.dto.response.ProductoResponseDTO;
import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.entity.Producto;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.CategoriaRepository;
import com.example.proyectodbp.repository.ProductoRepository;
import com.example.proyectodbp.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                                CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + dto.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCostoUnitario(dto.getCostoUnitario());
        producto.setCategoria(categoria);

        return toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoResponseDTO getById(Long id) {
        return toResponseDTO(productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id)));
    }

    @Override
    public List<ProductoResponseDTO> getAll() {
        return productoRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + dto.getCategoriaId()));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCostoUnitario(dto.getCostoUnitario());
        producto.setCategoria(categoria);

        return toResponseDTO(producto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoResponseDTO> getByCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoría no encontrada con id: " + categoriaId);
        }
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private ProductoResponseDTO toResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setCostoUnitario(producto.getCostoUnitario());
        dto.setCategoriaId(producto.getCategoria().getId());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());
        return dto;
    }
}
