package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArqueoResumenResponse {
    private BigDecimal esperadoEfectivo;
    private BigDecimal esperadoTarjeta;
    private BigDecimal esperadoDigital;
    private BigDecimal totalGeneral;
    private String sedeNombre;
    private String nombreCajero;
}