package com.example.proyectodbp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;

    @PositiveOrZero
    @Column(nullable = false)
    private int cantidadDisponible;

    @PositiveOrZero
    @Column(nullable = false)
    private int stockMinimo;
    // cuanto de esto tengo que tener minimo antes de que necesite comprar mas


}
