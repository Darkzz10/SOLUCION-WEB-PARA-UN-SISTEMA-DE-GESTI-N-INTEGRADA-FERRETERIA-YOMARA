package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class VentaDetalleRequest {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal descuento;
}
