package com.example.proyectodbp.repository;

import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.entity.Compra;
import com.example.proyectodbp.entity.EstadoCompra;
import com.example.proyectodbp.entity.Inventario;
import com.example.proyectodbp.entity.Producto;
import com.example.proyectodbp.entity.Rol;
import com.example.proyectodbp.entity.TipoDePago;
import com.example.proyectodbp.entity.Usuario;
import com.example.proyectodbp.entity.Venta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Test
    void shouldSaveAndFindCategoriaWhenValidData() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Bebidas");
        categoria.setDescripcion("Productos bebibles");

        Categoria saved = categoriaRepository.save(categoria);

        Optional<Categoria> found = categoriaRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Bebidas");
    }

    @Test
    void shouldFindProductosWhenCategoriaIdExists() {
        Categoria categoria = createCategoria("Snacks");
        Producto producto = createProducto("Galleta", categoria);
        productoRepository.save(producto);

        List<Producto> productos = productoRepository.findByCategoriaId(categoria.getId());

        assertThat(productos).hasSize(1);
        assertThat(productos.get(0).getNombre()).isEqualTo("Galleta");
    }

    @Test
    void shouldFindUsuarioWhenEmailExists() {
        Usuario usuario = createUsuario("admin@test.com", Rol.ADMIN);
        usuarioRepository.save(usuario);

        Optional<Usuario> found = usuarioRepository.findByEmail("admin@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getRol()).isEqualTo(Rol.ADMIN);
    }

    @Test
    void shouldFindInventarioWhenProductoIdExists() {
        Categoria categoria = createCategoria("Limpieza");
        Producto producto = productoRepository.save(createProducto("Jabón", categoria));
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidadDisponible(20);
        inventario.setStockMinimo(5);
        inventarioRepository.save(inventario);

        Optional<Inventario> found = inventarioRepository.findByProductoId(producto.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCantidadDisponible()).isEqualTo(20);
    }

    @Test
    void shouldFindComprasWhenUsuarioIdExists() {
        Usuario usuario = usuarioRepository.save(createUsuario("buyer@test.com", Rol.USER));
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setTotal(100.0);
        compra.setFecha(LocalDateTime.now());
        compra.setEstado(EstadoCompra.PENDIENTE);
        compraRepository.save(compra);

        List<Compra> compras = compraRepository.findByUsuarioId(usuario.getId());

        assertThat(compras).hasSize(1);
        assertThat(compras.get(0).getTotal()).isEqualTo(100.0);
    }

    @Test
    void shouldFindVentasWhenUsuarioIdExists() {
        Usuario usuario = usuarioRepository.save(createUsuario("seller@test.com", Rol.USER));
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setClienteNombre("Cliente prueba");
        venta.setTipo(TipoDePago.EFECTIVO);
        venta.setTotal(50.0);
        venta.setFecha(LocalDateTime.now());
        ventaRepository.save(venta);

        List<Venta> ventas = ventaRepository.findByUsuarioId(usuario.getId());

        assertThat(ventas).hasSize(1);
        assertThat(ventas.get(0).getClienteNombre()).isEqualTo("Cliente prueba");
    }

    private Categoria createCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion("Descripción");
        return categoriaRepository.save(categoria);
    }

    private Producto createProducto(String nombre, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion("Descripción");
        producto.setPrecio(10.0);
        producto.setCostoUnitario(5.0);
        producto.setCategoria(categoria);
        return producto;
    }

    private Usuario createUsuario(String email, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setEmail(email);
        usuario.setPassword("password");
        usuario.setRol(rol);
        return usuario;
    }
}
