package com.example.proyectodbp.controller;

import com.example.proyectodbp.dto.request.InventarioRequestDTO;
import com.example.proyectodbp.dto.response.InventarioResponseDTO;
import com.example.proyectodbp.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventarioResponseDTO>> getAll() {
        return ResponseEntity.ok(inventarioService.getAll());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioResponseDTO> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.getByProducto(productoId));
    }

    @GetMapping("/bajo-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventarioResponseDTO>> getLowStock() {
        return ResponseEntity.ok(inventarioService.getLowStock());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponseDTO> updateStock(@PathVariable Long id,
                                                              @Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.ok(inventarioService.updateStock(id, dto));
    }
}
