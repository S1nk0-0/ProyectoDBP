package com.example.proyectodbp.controller;

import com.example.proyectodbp.dto.response.CategoriaResponseDTO;
import com.example.proyectodbp.dto.response.InventarioResponseDTO;
import com.example.proyectodbp.dto.response.ProductoResponseDTO;
import com.example.proyectodbp.dto.response.UsuarioResponseDTO;
import com.example.proyectodbp.dto.response.VentaResponseDTO;
import com.example.proyectodbp.entity.Rol;
import com.example.proyectodbp.entity.TipoDePago;
import com.example.proyectodbp.service.CategoriaService;
import com.example.proyectodbp.service.CompraService;
import com.example.proyectodbp.service.InventarioService;
import com.example.proyectodbp.service.ProductoService;
import com.example.proyectodbp.service.UsuarioService;
import com.example.proyectodbp.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        CategoriaController.class,
        ProductoController.class,
        UsuarioController.class,
        InventarioController.class,
        VentaController.class,
        CompraController.class
})
@AutoConfigureMockMvc(addFilters = false)
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private InventarioService inventarioService;

    @MockitoBean
    private VentaService ventaService;

    @MockitoBean
    private CompraService compraService;

    @Test
    void shouldCreateCategoriaWhenRequestIsValid() throws Exception {
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        response.setId(1L);
        response.setNombre("Bebidas");
        response.setDescripcion("Productos bebibles");
        when(categoriaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Bebidas\",\"descripcion\":\"Productos bebibles\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Bebidas"));
    }

    @Test
    void shouldReturnBadRequestWhenCategoriaRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Sin nombre\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetProductoByIdWhenExists() throws Exception {
        ProductoResponseDTO response = new ProductoResponseDTO();
        response.setId(1L);
        response.setNombre("Galleta");
        response.setPrecio(3.5);
        response.setCostoUnitario(2.0);
        response.setCategoriaId(1L);
        response.setCategoriaNombre("Snacks");
        when(productoService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Galleta"))
                .andExpect(jsonPath("$.categoriaNombre").value("Snacks"));
    }

    @Test
    void shouldUpdateProductoWhenRequestIsValid() throws Exception {
        ProductoResponseDTO response = new ProductoResponseDTO();
        response.setId(1L);
        response.setNombre("Galleta actualizada");
        response.setPrecio(4.0);
        response.setCostoUnitario(2.0);
        response.setCategoriaId(1L);
        response.setCategoriaNombre("Snacks");
        when(productoService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoriaId\":1,\"nombre\":\"Galleta actualizada\",\"precio\":4.0,\"costoUnitario\":2.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Galleta actualizada"));
    }

    @Test
    void shouldRegisterUsuarioWhenRequestIsValid() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Mica");
        response.setEmail("mica@test.com");
        response.setRol(Rol.USER);
        when(usuarioService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Mica\",\"email\":\"mica@test.com\",\"password\":\"123456\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("mica@test.com"));
    }

    @Test
    void shouldReturnInventarioLowStockWhenRequested() throws Exception {
        InventarioResponseDTO response = new InventarioResponseDTO();
        response.setId(1L);
        response.setProductoId(1L);
        response.setProductoNombre("Producto");
        response.setCantidadDisponible(2);
        response.setStockMinimo(5);
        when(inventarioService.getLowStock()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/inventario/bajo-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoNombre").value("Producto"));
    }

    @Test
    void shouldCreateVentaWhenRequestIsValid() throws Exception {
        VentaResponseDTO response = new VentaResponseDTO();
        response.setId(1L);
        response.setUsuarioId(1L);
        response.setClienteNombre("Cliente");
        response.setTipo(TipoDePago.EFECTIVO);
        response.setTotal(20.0);
        when(ventaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteNombre\":\"Cliente\",\"tipo\":\"EFECTIVO\",\"detalles\":[{\"productoId\":1,\"cantidad\":2}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(20.0));
    }

    @Test
    void shouldDeleteVentaWhenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ventas/1"))
                .andExpect(status().isNoContent());

        verify(ventaService).delete(1L);
    }
}
