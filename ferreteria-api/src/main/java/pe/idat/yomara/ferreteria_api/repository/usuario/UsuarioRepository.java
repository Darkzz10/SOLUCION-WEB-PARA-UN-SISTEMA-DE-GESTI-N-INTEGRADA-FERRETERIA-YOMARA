package pe.idat.yomara.ferreteria_api.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
