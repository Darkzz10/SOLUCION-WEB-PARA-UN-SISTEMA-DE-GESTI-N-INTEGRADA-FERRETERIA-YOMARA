package pe.idat.yomara.ferreteria_api.repository.inventario;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.yomara.ferreteria_api.model.entity.logistica.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Traer todos los proveedores que no han sido eliminados lógicamente
    List<Proveedor> findByActivoTrue();

    // Buscar un proveedor activo por ID
    Optional<Proveedor> findByIdAndActivoTrue(Long id);

    // Validar si el RUC ya está registrado para no tener duplicados
    boolean existsByRuc(String ruc);
}