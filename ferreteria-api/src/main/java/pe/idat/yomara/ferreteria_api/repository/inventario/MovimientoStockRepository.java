package pe.idat.yomara.ferreteria_api.repository.inventario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.MovimientoStock;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    @Query("SELECT m FROM MovimientoStock m WHERE " +
            "(:sedeId IS NULL OR m.sede.id = :sedeId) AND " +
            "(:tipo IS NULL OR m.tipoMovimiento = :tipo) AND " +
            "(CAST(:inicio AS timestamp) IS NULL OR m.fechaMovimiento >= :inicio) AND " +
            "(CAST(:fin AS timestamp) IS NULL OR m.fechaMovimiento <= :fin) " +
            "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoStock> buscarConFiltros(
            @Param("sedeId") Long sedeId,
            @Param("tipo") TipoMovimiento tipo,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    List<MovimientoStock> findByProductoId(Long productoId);
}
