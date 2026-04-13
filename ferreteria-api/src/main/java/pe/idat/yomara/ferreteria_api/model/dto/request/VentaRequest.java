package pe.idat.yomara.ferreteria_api.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Venta;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
public class VentaRequest {

    @NotBlank(message = "El tipo de comprobante es obligatorio")
    private String tipoComprobante; // BOLETA o FACTURA

    private BigDecimal descuentoTotal; // Puede ser 0 o nulo

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotEmpty(message = "La venta debe tener al menos un producto")
    private List<VentaDetalleRequest> detalles;

    // PIEZAS PARA LOGÍSTICA
    @NotNull(message = "Debe especificar si es entrega en TIENDA o DOMICILIO")
    private Venta.TipoEntrega tipoEntrega;

    private String direccionEntrega; // Se valida en el Service: si es DOMICILIO, este no debe ser nulo

    // MEDIO DE PAGO (Agregado para completar el flujo)
    @NotNull(message = "El medio de pago es obligatorio")
    private Venta.MedioPago medioPago;
}