package pe.idat.yomara.ferreteria_api.service.usuario.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // IMPORTANTE: Spring Security requiere que los roles empiecen con "ROLE_"
        String nombreRol = "ROLE_" + usuario.getRol().name();

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getActivo(), // Si el usuario está inactivo, no podrá loguearse
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(nombreRol))
        );
    }
}
