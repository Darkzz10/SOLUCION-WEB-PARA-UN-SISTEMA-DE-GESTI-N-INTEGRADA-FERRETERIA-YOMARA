package pe.idat.yomara.ferreteria_api.service.auth;

import pe.idat.yomara.ferreteria_api.model.dto.request.LoginRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
