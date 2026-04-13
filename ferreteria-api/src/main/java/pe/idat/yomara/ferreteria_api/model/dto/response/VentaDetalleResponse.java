package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class VentaDetalleResponse {
    private Long id;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private Long productoId;
    private String productoNombre;
    private String productoSku;
}
