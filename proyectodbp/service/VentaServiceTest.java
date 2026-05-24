package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.DetalleDeVentaRequestDTO;
import com.example.proyectodbp.dto.request.VentaRequestDTO;
import com.example.proyectodbp.dto.response.VentaResponseDTO;
import com.example.proyectodbp.entity.Categoria;
import com.example.proyectodbp.entity.Inventario;
import com.example.proyectodbp.entity.Producto;
import com.example.proyectodbp.entity.Rol;
import com.example.proyectodbp.entity.TipoDePago;
import com.example.proyectodbp.entity.Usuario;
import com.example.proyectodbp.entity.Venta;
import com.example.proyectodbp.exception.InsufficientStockException;
import com.example.proyectodbp.repository.DetalleDeVentaRepository;
import com.example.proyectodbp.repository.InventarioRepository;
import com.example.proyectodbp.repository.ProductoRepository;
import com.example.proyectodbp.repository.UsuarioRepository;
import com.example.proyectodbp.repository.VentaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleDeVentaRepository detalleDeVentaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private VentaService ventaService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldCreateVentaAndDiscountStockWhenStockIsAvailable() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@test.com", null));

        Usuario usuario = createUsuario();
        Producto producto = createProducto();
        Inventario inventario = createInventario(producto, 10);
        Venta savedVenta = new Venta();
        savedVenta.setId(1L);
        savedVenta.setUsuario(usuario);
        savedVenta.setClienteNombre("Cliente");
        savedVenta.setTipo(TipoDePago.EFECTIVO);
        savedVenta.setTotal(20.0);

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto(producto)).thenReturn(Optional.of(inventario));
        when(ventaRepository.save(any(Venta.class))).thenReturn(savedVenta);
        when(detalleDeVentaRepository.saveAll(any())).thenReturn(List.of());

        VentaResponseDTO response = ventaService.create(createRequest(2));

        assertThat(response.getTotal()).isEqualTo(20.0);
        assertThat(inventario.getCantidadDisponible()).isEqualTo(8);
    }

    @Test
    void shouldThrowInsufficientStockWhenStockIsNotAvailable() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@test.com", null));

        Usuario usuario = createUsuario();
        Producto producto = createProducto();
        Inventario inventario = createInventario(producto, 1);

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto(producto)).thenReturn(Optional.of(inventario));

        assertThatThrownBy(() -> ventaService.create(createRequest(3)))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    private VentaRequestDTO createRequest(int cantidad) {
        DetalleDeVentaRequestDTO detalle = new DetalleDeVentaRequestDTO();
        detalle.setProductoId(1L);
        detalle.setCantidad(cantidad);

        VentaRequestDTO request = new VentaRequestDTO();
        request.setClienteNombre("Cliente");
        request.setTipo(TipoDePago.EFECTIVO);
        request.setDetalles(List.of(detalle));
        return request;
    }

    private Usuario createUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario");
        usuario.setEmail("user@test.com");
        usuario.setPassword("password");
        usuario.setRol(Rol.USER);
        return usuario;
    }

    private Producto createProducto() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("General");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto");
        producto.setPrecio(10.0);
        producto.setCostoUnitario(5.0);
        producto.setCategoria(categoria);
        return producto;
    }

    private Inventario createInventario(Producto producto, int cantidad) {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProducto(producto);
        inventario.setCantidadDisponible(cantidad);
        inventario.setStockMinimo(2);
        return inventario;
    }
}
