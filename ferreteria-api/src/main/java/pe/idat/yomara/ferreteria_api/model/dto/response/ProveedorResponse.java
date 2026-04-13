package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;

@Data
public class ProveedorResponse {
    private Long id;
    private String ruc;
    private String razonSocial;
    private String contacto;
    private String email;
    private String telefono;
    private Boolean activo;
}
