package com.example.proyectodbp.controller;

import com.example.proyectodbp.dto.ProductoRequestDTO;
import com.example.proyectodbp.dto.ProductoResponseDTO;
import com.example.proyectodbp.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAll() {
        return ResponseEntity.ok(productoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> update(@PathVariable Long id,
                                                       @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.getByCategoria(categoriaId));
    }
}
