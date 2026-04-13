package pe.idat.yomara.ferreteria_api.service.usuario.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.UsuarioRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.SedeResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.UsuarioDetalleResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.UsuarioResponse;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.repository.usuario.SedeRepository;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.service.usuario.UsuarioService;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Inyectamos el encriptador
    private final SedeRepository sedeRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,SedeRepository sedeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.sedeRepository = sedeRepository;
    }



    @Override
    public UsuarioResponse crear(UsuarioRequest request) {
        // 1. Convertir Request -> Entidad
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());

        // ¡IMPORTANTE! Encriptar la clave antes de guardar
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setRol(request.getRol());
        usuario.setActivo(true);

        // 2. REGLA DE ORO: Validar que el sedeId no sea nulo antes de buscar
        if (request.getSedeId() == null) {
            throw new RuntimeException("Error: La sede es obligatoria para registrar un usuario.");
        }

        // 3. Buscar la Sede y asignarla (esto llena la columna sede_id en la BD)
        Sede sede = sedeRepository.findById(request.getSedeId())
                .orElseThrow(() -> new RuntimeException("Error: No existe una sede con el ID " + request.getSedeId()));

        usuario.setSede(sede);

        // 4. Guardar y responder
        Usuario guardado = usuarioRepository.save(usuario);
        return convertirADto(guardado);
    }

    @Override
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirADto(usuario);
    }

    @Override
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    @Override
    public List<UsuarioDetalleResponse> listarColaboradores() {
        return usuarioRepository.findAll().stream().map(u -> {
            UsuarioDetalleResponse dto = new UsuarioDetalleResponse();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setRol(u.getRol().name());
            dto.setSedeNombre(u.getSede().getNombre());
            dto.setActivo(u.getActivo());
            return dto;
        }).toList();
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setActivo(false); // "Borrado" lógico por seguridad de auditoría
        usuarioRepository.save(u);
    }

    // MÉTODO AYUDANTE DE MAPEADO
    private UsuarioResponse convertirADto(Usuario usuario) {
        UsuarioResponse resp = new UsuarioResponse();
        resp.setId(usuario.getId());
        resp.setUsername(usuario.getUsername());
        resp.setNombreCompleto(usuario.getNombreCompleto());
        resp.setRol(usuario.getRol());
        resp.setActivo(usuario.getActivo());

        if (usuario.getSede() != null) {
            resp.setSedeId(usuario.getSede().getId());
        }

        return resp;
    }
}