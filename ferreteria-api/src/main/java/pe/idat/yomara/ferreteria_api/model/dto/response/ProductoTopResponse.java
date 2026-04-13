package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductoTopResponse {
    private String nombreProducto;
    private Long cantidadVendida;
    private BigDecimal ingresosGenerados;
}
