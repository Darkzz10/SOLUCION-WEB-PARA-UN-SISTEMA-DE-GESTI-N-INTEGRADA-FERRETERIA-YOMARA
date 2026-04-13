package pe.idat.yomara.ferreteria_api.repository.inventario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Producto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsBySku(String sku);
    boolean existsByNombreIgnoreCase(String nombre);

    //  El buscador mágico para el vendedor
    List<Producto> findByActivoTrueAndNombreContainingIgnoreCaseOrActivoTrueAndSkuContainingIgnoreCase(String nombre, String sku);
    //  Listar solo los activos para el catálogo general
    List<Producto> findByActivoTrue();
    Optional<Producto> findBySku(String sku);
}