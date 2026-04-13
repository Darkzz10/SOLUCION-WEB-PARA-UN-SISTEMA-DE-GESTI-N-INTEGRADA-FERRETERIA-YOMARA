package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SedeResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
}
