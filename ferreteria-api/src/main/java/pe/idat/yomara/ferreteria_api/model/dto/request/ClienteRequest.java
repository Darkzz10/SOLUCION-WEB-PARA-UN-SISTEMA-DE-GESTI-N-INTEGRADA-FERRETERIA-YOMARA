package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteRequest {
    private String documentoNumero;
    private String nombreCompleto;
    private String email;
    private String telefono;
}
