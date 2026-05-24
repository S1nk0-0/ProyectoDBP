package com.example.proyectodbp.repository;

import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.entity.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class RepositoryTestcontainersTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void shouldSaveAndFindProductoWhenUsingPostgresContainer() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Testcontainers");
        categoria.setDescripcion("Categoria de prueba");
        Categoria savedCategoria = categoriaRepository.save(categoria);

        Producto producto = new Producto();
        producto.setNombre("Producto Postgres");
        producto.setDescripcion("Producto de prueba");
        producto.setPrecio(12.0);
        producto.setCostoUnitario(8.0);
        producto.setCategoria(savedCategoria);
        Producto savedProducto = productoRepository.save(producto);

        assertThat(productoRepository.findById(savedProducto.getId())).isPresent();
        assertThat(productoRepository.findByCategoriaId(savedCategoria.getId())).hasSize(1);
    }
}
