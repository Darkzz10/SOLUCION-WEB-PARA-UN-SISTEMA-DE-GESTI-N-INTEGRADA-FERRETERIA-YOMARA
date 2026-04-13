package pe.idat.yomara.ferreteria_api.controller.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.idat.yomara.ferreteria_api.model.dto.request.LoginRequest;
import pe.idat.yomara.ferreteria_api.model.dto.request.UsuarioRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.AuthResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.UsuarioResponse;
import pe.idat.yomara.ferreteria_api.service.auth.AuthService;
import pe.idat.yomara.ferreteria_api.service.usuario.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public AuthController(UsuarioService usuarioService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse nuevoUsuario = usuarioService.crear(request);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}