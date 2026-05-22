package com.example.proyectodbp.service.impl;

import com.example.proyectodbp.dto.request.CompraRequestDTO;
import com.example.proyectodbp.dto.response.CompraResponseDTO;
import com.example.proyectodbp.dto.request.DetalleCompraRequestDTO;
import com.example.proyectodbp.dto.response.DetalleCompraResponseDTO;
import com.example.proyectodbp.entity.*;
import com.example.proyectodbp.exception.ResourceNotFoundException;
import com.example.proyectodbp.repository.*;
import com.example.proyectodbp.service.CompraService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final DetalleDeCompraRepository detalleCompraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                              DetalleDeCompraRepository detalleCompraRepository,
                              UsuarioRepository usuarioRepository,
                              ProductoRepository productoRepository,
                              InventarioRepository inventarioRepository) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    @Transactional
    public CompraResponseDTO create(CompraRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        double total = 0.0;
        List<DetalleCompra> detalles = new ArrayList<>();

        for (DetalleCompraRequestDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con id: " + detalleDTO.getProductoId()));

            Inventario inventario = inventarioRepository.findByProducto(producto)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventario no encontrado para: " + producto.getNombre()));

            inventario.setCantidadDisponible(inventario.getCantidadDisponible() + detalleDTO.getCantidad());
            inventarioRepository.save(inventario);

            double subtotal = detalleDTO.getPrecioUnitario() * detalleDTO.getCantidad();
            total += subtotal;

            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
            detalles.add(detalle);
        }

        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setFecha(LocalDateTime.now());
        compra.setTotal(total);
        compra.setEstado(EstadoCompra.PENDIENTE);
        Compra savedCompra = compraRepository.save(compra);

        detalles.forEach(d -> d.setCompra(savedCompra));
        List<DetalleCompra> savedDetalles = detalleCompraRepository.saveAll(detalles);
        savedCompra.setDetalles(savedDetalles);

        return toResponseDTO(savedCompra);
    }

    @Override
    public CompraResponseDTO getById(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id: " + id));
        checkOwnershipOrAdmin(compra);
        return toResponseDTO(compra);
    }

    @Override
    public List<CompraResponseDTO> getAll() {
        return compraRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<CompraResponseDTO> getMisCompras() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return compraRepository.findByUsuarioId(usuario.getId()).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<CompraResponseDTO> getByUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        return compraRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public CompraResponseDTO updateEstado(Long id, EstadoCompra estado) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id: " + id));
        compra.setEstado(estado);
        return toResponseDTO(compra);
    }

    private void checkOwnershipOrAdmin(Compra compra) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario currentUser = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (!compra.getUsuario().getId().equals(currentUser.getId()) && currentUser.getRol() != Rol.ADMIN) {
            throw new AccessDeniedException("No tienes permiso para acceder a esta compra");
        }
    }

    private CompraResponseDTO toResponseDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setUsuarioId(compra.getUsuario().getId());
        dto.setTotal(compra.getTotal());
        dto.setFecha(compra.getFecha());
        dto.setEstado(compra.getEstado());
        if (compra.getDetalles() != null) {
            dto.setDetalles(compra.getDetalles().stream().map(this::toDetalleResponseDTO).toList());
        }
        return dto;
    }

    private DetalleCompraResponseDTO toDetalleResponseDTO(DetalleCompra detalle) {
        DetalleCompraResponseDTO dto = new DetalleCompraResponseDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProducto().getId());
        dto.setProductoNombre(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
