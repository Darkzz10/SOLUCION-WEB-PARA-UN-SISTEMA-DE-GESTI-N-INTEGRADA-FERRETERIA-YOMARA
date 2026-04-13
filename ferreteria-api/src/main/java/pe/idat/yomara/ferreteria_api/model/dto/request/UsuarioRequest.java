package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Rol;

@Data
@NoArgsConstructor
public class UsuarioRequest {
    private String username;
    private String password;
    private String nombreCompleto;
    private Rol rol;
    private Long sedeId;
}
