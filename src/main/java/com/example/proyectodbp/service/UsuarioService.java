package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.LoginRequestDTO;
import com.example.proyectodbp.dto.response.LoginResponseDTO;
import com.example.proyectodbp.dto.request.UsuarioRequestDTO;
import com.example.proyectodbp.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO register(UsuarioRequestDTO dto);
    LoginResponseDTO login(LoginRequestDTO dto);
    UsuarioResponseDTO getById(Long id);
    List<UsuarioResponseDTO> getAll();
    UsuarioResponseDTO updateProfile(Long id, UsuarioRequestDTO dto);
    void delete(Long id);
}
