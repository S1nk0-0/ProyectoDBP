package com.example.proyectodbp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 150)
    private String clienteNombre;

    @OneToMany(mappedBy = "venta")
    private List<DetalleDeVenta> detalles = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDePago tipo;

    @Positive
    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private Double total;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;
}
