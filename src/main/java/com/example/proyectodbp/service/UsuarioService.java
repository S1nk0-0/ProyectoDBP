package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.LoginRequestDTO;
import com.example.proyectodbp.dto.LoginResponseDTO;
import com.example.proyectodbp.dto.UsuarioRequestDTO;
import com.example.proyectodbp.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO register(UsuarioRequestDTO dto);
    LoginResponseDTO login(LoginRequestDTO dto);
    UsuarioResponseDTO getById(Long id);
    List<UsuarioResponseDTO> getAll();
    UsuarioResponseDTO updateProfile(Long id, UsuarioRequestDTO dto);
    void delete(Long id);
}
