package pe.idat.yomara.ferreteria_api.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {
    boolean existsByNombre(String nombre);
}