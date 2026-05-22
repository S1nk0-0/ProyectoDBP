package com.example.proyectodbp.service.impl;

import com.example.proyectodbp.dto.request.LoginRequestDTO;
import com.example.proyectodbp.dto.response.LoginResponseDTO;
import com.example.proyectodbp.dto.request.UsuarioRequestDTO;
import com.example.proyectodbp.dto.response.UsuarioResponseDTO;
import com.example.proyectodbp.entity.Rol;
import com.example.proyectodbp.entity.Usuario;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.UsuarioRepository;
import com.example.proyectodbp.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder,
                               AuthenticationManager authenticationManager,
                               JwtEncoder jwtEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO register(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(dto.getRol() != null ? dto.getRol() : Rol.USER);
        return toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sellio")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(86400))
                .subject(dto.getEmail())
                .claim("userId", usuario.getId())
                .claim("rol", usuario.getRol().name())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        return response;
    }

    @Override
    public UsuarioResponseDTO getById(Long id) {
        return toResponseDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id)));
    }

    @Override
    public List<UsuarioResponseDTO> getAll() {
        return usuarioRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updateProfile(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        return toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setRol(usuario.getRol());
        return dto;
    }
}
