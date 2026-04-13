package pe.idat.yomara.ferreteria_api.controller.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.response.UsuarioDetalleResponse;
import pe.idat.yomara.ferreteria_api.service.usuario.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDetalleResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarColaboradores());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok("Usuario desactivado correctamente.");
    }
}
