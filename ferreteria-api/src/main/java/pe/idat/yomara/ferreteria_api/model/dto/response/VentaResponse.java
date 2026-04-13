package pe.idat.yomara.ferreteria_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class VentaResponse {
    private Long id;
    private String codigoOperacion;
    private String tipoComprobante;
    private String medioPago;
    private BigDecimal subtotal;
    private BigDecimal descuentoTotal;
    private BigDecimal impuesto;
    private BigDecimal total;
    private String estado;
    private String estadoVenta; // El Enum (PENDIENTE_DESPACHO, ENTREGADO, etc.)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaVenta;

    //  CAMPOS DE LOGÍSTICA (Nuevos en el DTO)
    private String tipoEntrega;      // TIENDA o DOMICILIO
    private String direccionEntrega;
    private String evidenciaEntrega; // URL o Base64 de la foto

    private Long clienteId;
    private String clienteDocumento;
    private String clienteNombre;

    private Long vendedorId;
    private String vendedorNombre;

    private Long sedeId;
    private String sedeNombre;

    private List<VentaDetalleResponse> detalles;
}
