package pe.idat.yomara.ferreteria_api.repository.inventario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.StockSede;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockSedeRepository extends JpaRepository<StockSede, Long> {
    List<StockSede> findByProductoId(Long productoId);
    Optional<StockSede> findByProductoIdAndSedeId(Long productoId, Long sedeId);
}
