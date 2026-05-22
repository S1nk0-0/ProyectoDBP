package com.example.proyectodbp.controller;

import com.example.proyectodbp.dto.request.CompraRequestDTO;
import com.example.proyectodbp.dto.response.CompraResponseDTO;
import com.example.proyectodbp.entity.EstadoCompra;
import com.example.proyectodbp.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> create(@Valid @RequestBody CompraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompraResponseDTO>> getAll() {
        return ResponseEntity.ok(compraService.getAll());
    }

    @GetMapping("/mias")
    public ResponseEntity<List<CompraResponseDTO>> getMisCompras() {
        return ResponseEntity.ok(compraService.getMisCompras());
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompraResponseDTO>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(compraService.getByUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(compraService.getById(id));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompraResponseDTO> updateEstado(@PathVariable Long id,
                                                           @RequestParam EstadoCompra estado) {
        return ResponseEntity.ok(compraService.updateEstado(id, estado));
    }
}
