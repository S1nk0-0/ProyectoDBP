package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.CategoriaRequestDTO;
import com.example.proyectodbp.dto.response.CategoriaResponseDTO;
import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void shouldCreateCategoriaWhenValidRequest() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Bebidas");
        request.setDescripcion("Productos bebibles");

        Categoria saved = new Categoria();
        saved.setId(1L);
        saved.setNombre("Bebidas");
        saved.setDescripcion("Productos bebibles");
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(saved);

        CategoriaResponseDTO response = categoriaService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Bebidas");
    }

    @Test
    void shouldThrowResourceNotFoundWhenCategoriaDoesNotExist() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoría no encontrada");
    }

    @Test
    void shouldDeleteCategoriaWhenIdExists() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.delete(1L);

        verify(categoriaRepository).deleteById(1L);
    }
}
