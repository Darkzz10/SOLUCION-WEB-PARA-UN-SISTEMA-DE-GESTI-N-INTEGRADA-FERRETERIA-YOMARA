package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;

@Data
public class UsuarioDetalleResponse {
    private Long id;
    private String username;
    private String nombreCompleto;
    private String rol;
    private String sedeNombre;
    private Boolean activo;
}
