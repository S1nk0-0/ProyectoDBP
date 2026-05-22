package com.example.proyectodbp.dto.request;

import com.example.proyectodbp.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String telefono;
    private String direccion;
    private Rol rol;
}
