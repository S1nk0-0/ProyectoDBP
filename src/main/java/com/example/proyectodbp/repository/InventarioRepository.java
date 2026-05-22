package com.example.proyectodbp.repository;

import com.example.proyectodbp.entity.Inventario;
import com.example.proyectodbp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProducto(Producto producto);
    Optional<Inventario> findByProductoId(Long productoId);
}
