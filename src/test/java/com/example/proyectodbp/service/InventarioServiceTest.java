package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.InventarioRequestDTO;
import com.example.proyectodbp.dto.response.InventarioResponseDTO;
import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.entity.Inventario;
import com.example.proyectodbp.entity.Producto;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void shouldUpdateStockWhenInventarioExists() {
        Inventario inventario = createInventario(1L, 5, 2);
        InventarioRequestDTO request = new InventarioRequestDTO();
        request.setCantidadDisponible(15);
        request.setStockMinimo(4);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        InventarioResponseDTO response = inventarioService.updateStock(1L, request);

        assertThat(response.getCantidadDisponible()).isEqualTo(15);
        assertThat(response.getStockMinimo()).isEqualTo(4);
    }

    @Test
    void shouldReturnLowStockWhenCantidadIsLessOrEqualThanMinimo() {
        Inventario lowStock = createInventario(1L, 2, 5);
        Inventario normalStock = createInventario(2L, 20, 5);
        when(inventarioRepository.findAll()).thenReturn(List.of(lowStock, normalStock));

        List<InventarioResponseDTO> response = inventarioService.getLowStock();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getCantidadDisponible()).isEqualTo(2);
    }

    @Test
    void shouldThrowResourceNotFoundWhenInventarioDoesNotExist() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventarioService.getByProducto(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Inventario no encontrado");
    }

    private Inventario createInventario(Long id, int cantidad, int minimo) {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("General");

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Producto " + id);
        producto.setPrecio(10.0);
        producto.setCostoUnitario(5.0);
        producto.setCategoria(categoria);

        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setProducto(producto);
        inventario.setCantidadDisponible(cantidad);
        inventario.setStockMinimo(minimo);
        return inventario;
    }
}
