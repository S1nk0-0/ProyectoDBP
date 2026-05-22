package com.example.proyectodbp.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {}

//
// TODO: SecurityFilterChain, AuthenticationManager, KeyPair, JwtDecoder, JwtEncoder, JwtAuthenticationConverter, nose que mas

