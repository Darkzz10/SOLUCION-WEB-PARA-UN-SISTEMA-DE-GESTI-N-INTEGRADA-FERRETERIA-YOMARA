package pe.idat.yomara.ferreteria_api.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.yomara.ferreteria_api.model.entity.venta.ArqueoCaja;

import java.time.LocalDateTime;

public interface ArqueoCajaRepository extends JpaRepository<ArqueoCaja, Long> {
    // Valida si ya existe un arqueo en esta sede entre dos horas (inicio y fin del día)
    boolean existsBySedeIdAndFechaCierreBetween(Long sedeId, LocalDateTime inicio, LocalDateTime fin);
}
