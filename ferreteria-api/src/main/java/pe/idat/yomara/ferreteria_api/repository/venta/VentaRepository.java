package pe.idat.yomara.ferreteria_api.repository.venta;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.idat.yomara.ferreteria_api.model.entity.venta.EstadoVenta;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findBySedeIdAndEstadoVenta(Long sedeId, EstadoVenta estadoVenta);
    //  Sumar todo el dinero de las ventas del mes
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaVenta >= :inicio AND v.fechaVenta <= :fin AND v.estado = 'COMPLETADA'")
    BigDecimal calcularIngresosDelMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    //  Contar cuántas ventas (tickets) se emitieron
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta >= :inicio AND v.fechaVenta <= :fin AND v.estado = 'COMPLETADA'")
    Integer contarVentasDelMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    //  El Top de Productos más vendidos (Agrupando por nombre y sumando cantidades)
    @Query("SELECT d.producto.nombre, SUM(d.cantidad), SUM(d.subtotal) " +
            "FROM VentaDetalle d JOIN d.venta v " +
            "WHERE v.fechaVenta >= :inicio AND v.fechaVenta <= :fin AND v.estado = 'COMPLETADA' " +
            "GROUP BY d.producto.nombre " +
            "ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> obtenerTopProductosDelMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin, Pageable pageable);

    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.sede.id = :sedeId " +
            "AND v.fechaVenta >= :inicio AND v.fechaVenta <= :fin " +
            "AND v.medioPago = :medioPago AND v.estado = 'COMPLETADA'")
    BigDecimal sumarTotalPorSedeYMedioPago(
            @Param("sedeId") Long sedeId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("medioPago") Venta.MedioPago medioPago);

}