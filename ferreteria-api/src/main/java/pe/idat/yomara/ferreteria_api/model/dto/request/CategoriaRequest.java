package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CategoriaRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
}
