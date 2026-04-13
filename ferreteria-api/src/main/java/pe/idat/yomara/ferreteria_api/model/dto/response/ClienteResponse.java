package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteResponse {
    private Long id;
    private String documentoNumero;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Boolean activo;
}
