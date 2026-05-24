package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.ProductoRequestDTO;
import com.example.proyectodbp.dto.response.ProductoResponseDTO;
import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.entity.Producto;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.CategoriaRepository;
import com.example.proyectodbp.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void shouldCreateProductoWhenCategoriaExists() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Snacks");

        Producto saved = new Producto();
        saved.setId(10L);
        saved.setNombre("Galleta");
        saved.setDescripcion("Chocolate");
        saved.setPrecio(3.5);
        saved.setCostoUnitario(2.0);
        saved.setCategoria(categoria);

        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setCategoriaId(1L);
        request.setNombre("Galleta");
        request.setDescripcion("Chocolate");
        request.setPrecio(3.5);
        request.setCostoUnitario(2.0);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(saved);

        ProductoResponseDTO response = productoService.create(request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getCategoriaNombre()).isEqualTo("Snacks");
    }

    @Test
    void shouldThrowResourceNotFoundWhenCategoriaDoesNotExistOnCreate() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setCategoriaId(99L);
        request.setNombre("Producto");
        request.setPrecio(10.0);
        request.setCostoUnitario(5.0);
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoría no encontrada");
    }
}
