package pe.idat.yomara.ferreteria_api.repository.venta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Cliente;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDocumentoNumero(String documentoNumero);

    // Para la lista general con filtros (Paginado)
    Page<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre, Pageable pageable);

}
