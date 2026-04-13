package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Rol;

@Data
@NoArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private String nombreCompleto;
    private Rol rol;
    private Boolean activo;
    private Long sedeId;
}
