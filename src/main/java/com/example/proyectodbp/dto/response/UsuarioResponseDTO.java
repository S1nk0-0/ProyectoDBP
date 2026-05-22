package com.example.proyectodbp.dto.response;

import com.example.proyectodbp.entity.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private Rol rol;
}
