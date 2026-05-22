resumen del codigo hasta ahora: 
Java 21 jdk ms 21

Relaciones:
- Usuario → hace → Compra → DetalleCompra → Producto ↔ Inventario.
- Usuario → hace → Venta  → DetalleVenta  → Producto ↔ Inventario



Pendientes :
1. Repositorios por entidad.
2. DTOs —
3. Servicios — lógica: al registrar una Compra, actualizar Inventario.cantidadDisponible; al registrar una Venta, decrementarlo. dar una alerta cuando llega al stock minimo
5. Controladores REST 
6. application.properties configurar  PostgreSQL.
