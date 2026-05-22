package com.example.proyectodbp.controller;

import com.example.proyectodbp.dto.VentaRequestDTO;
import com.example.proyectodbp.dto.VentaResponseDTO;
import com.example.proyectodbp.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> create(@Valid @RequestBody VentaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaResponseDTO>> getAll() {
        return ResponseEntity.ok(ventaService.getAll());
    }

    @GetMapping("/mias")
    public ResponseEntity<List<VentaResponseDTO>> getMisVentas() {
        return ResponseEntity.ok(ventaService.getMisVentas());
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaResponseDTO>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ventaService.getByUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
