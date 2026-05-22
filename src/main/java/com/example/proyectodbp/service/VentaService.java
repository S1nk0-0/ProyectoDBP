package com.example.proyectodbp.service;

import com.example.proyectodbp.dto.request.DetalleDeVentaRequestDTO;
import com.example.proyectodbp.dto.response.DetalleDeVentaResponseDTO;
import com.example.proyectodbp.dto.request.VentaRequestDTO;
import com.example.proyectodbp.dto.response.VentaResponseDTO;
import com.example.proyectodbp.entity.DetalleDeVenta;
import com.example.proyectodbp.entity.Inventario;
import com.example.proyectodbp.entity.Producto;
import com.example.proyectodbp.entity.Rol;
import com.example.proyectodbp.entity.Usuario;
import com.example.proyectodbp.entity.Venta;
import com.example.proyectodbp.exception.InsufficientStockException;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.DetalleDeVentaRepository;
import com.example.proyectodbp.repository.InventarioRepository;
import com.example.proyectodbp.repository.ProductoRepository;
import com.example.proyectodbp.repository.UsuarioRepository;
import com.example.proyectodbp.repository.VentaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleDeVentaRepository detalleDeVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public VentaService(VentaRepository ventaRepository,
                        DetalleDeVentaRepository detalleDeVentaRepository,
                        UsuarioRepository usuarioRepository,
                        ProductoRepository productoRepository,
                        InventarioRepository inventarioRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleDeVentaRepository = detalleDeVentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional
    public VentaResponseDTO create(VentaRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        double total = 0.0;
        List<DetalleDeVenta> detalles = new ArrayList<>();

        for (DetalleDeVentaRequestDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con id: " + detalleDTO.getProductoId()));

            Inventario inventario = inventarioRepository.findByProducto(producto)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventario no encontrado para: " + producto.getNombre()));

            if (inventario.getCantidadDisponible() < detalleDTO.getCantidad()) {
                throw new InsufficientStockException(
                        "Stock insuficiente para '" + producto.getNombre() + "'. " +
                        "Disponible: " + inventario.getCantidadDisponible() +
                        ", solicitado: " + detalleDTO.getCantidad());
            }

            inventario.setCantidadDisponible(inventario.getCantidadDisponible() - detalleDTO.getCantidad());
            inventarioRepository.save(inventario);

            double subtotal = producto.getPrecio() * detalleDTO.getCantidad();
            total += subtotal;

            DetalleDeVenta detalle = new DetalleDeVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            detalles.add(detalle);
        }

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setClienteNombre(dto.getClienteNombre());
        venta.setTipo(dto.getTipo());
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(total);
        Venta savedVenta = ventaRepository.save(venta);

        detalles.forEach(d -> d.setVenta(savedVenta));
        List<DetalleDeVenta> savedDetalles = detalleDeVentaRepository.saveAll(detalles);
        savedVenta.setDetalles(savedDetalles);

        return toResponseDTO(savedVenta);
    }

    public VentaResponseDTO getById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
        checkOwnershipOrAdmin(venta);
        return toResponseDTO(venta);
    }

    public List<VentaResponseDTO> getAll() {
        return ventaRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public List<VentaResponseDTO> getMisVentas() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ventaRepository.findByUsuarioId(usuario.getId()).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<VentaResponseDTO> getByUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        return ventaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
        ventaRepository.delete(venta);
    }

    private void checkOwnershipOrAdmin(Venta venta) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario currentUser = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (!venta.getUsuario().getId().equals(currentUser.getId()) && currentUser.getRol() != Rol.ADMIN) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta venta");
        }
    }

    private VentaResponseDTO toResponseDTO(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setUsuarioId(venta.getUsuario().getId());
        dto.setClienteNombre(venta.getClienteNombre());
        dto.setTipo(venta.getTipo());
        dto.setTotal(venta.getTotal());
        dto.setFecha(venta.getFecha());
        if (venta.getDetalles() != null) {
            dto.setDetalles(venta.getDetalles().stream().map(this::toDetalleResponseDTO).toList());
        }
        return dto;
    }

    private DetalleDeVentaResponseDTO toDetalleResponseDTO(DetalleDeVenta detalle) {
        DetalleDeVentaResponseDTO dto = new DetalleDeVentaResponseDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProducto().getId());
        dto.setProductoNombre(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
