package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String rol;
    private Long sedeId;
    private String sedeNombre;
    private String nombreCompleto;
}