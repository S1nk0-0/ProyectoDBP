package com.example.proyectodbp.controller;

import com.example.proyectodbp.dto.LoginRequestDTO;
import com.example.proyectodbp.dto.LoginResponseDTO;
import com.example.proyectodbp.dto.UsuarioRequestDTO;
import com.example.proyectodbp.dto.UsuarioResponseDTO;
import com.example.proyectodbp.service.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.login(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateProfile(@PathVariable Long id,
                                                             @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.updateProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
