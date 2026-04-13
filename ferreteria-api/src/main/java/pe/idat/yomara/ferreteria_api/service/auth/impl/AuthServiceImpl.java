package pe.idat.yomara.ferreteria_api.service.auth.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.yomara.ferreteria_api.model.dto.request.LoginRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.AuthResponse;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.service.auth.AuthService;
import pe.idat.yomara.ferreteria_api.config.JwtProvider;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Validar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Verificar password
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // 3. Generar el Token
        String token = jwtProvider.generateToken(usuario);

        // 4. Construir respuesta con la data de la Sede (Pieza clave para Android)
        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .sedeId(usuario.getSede().getId())
                .sedeNombre(usuario.getSede().getNombre())
                .nombreCompleto(usuario.getNombreCompleto())
                .build();
    }
}