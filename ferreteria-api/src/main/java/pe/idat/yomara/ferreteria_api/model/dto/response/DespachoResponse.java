package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DespachoResponse {
    private Long ventaId;
    private String clienteNombre;
    private String clienteTelefono;
    private String direccionEntrega;
    private BigDecimal montoTotal;
    private String estado;
    private String fechaVenta;
}
