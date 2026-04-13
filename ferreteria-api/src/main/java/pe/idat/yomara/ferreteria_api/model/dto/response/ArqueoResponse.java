package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ArqueoResponse {
    private Long id;
    private LocalDateTime fechaCierre;
    private BigDecimal montoEsperado;
    private BigDecimal montoReal;
    private BigDecimal diferencia;
    private String observaciones;
    private String vendedorNombre;
    private String sedeNombre;
}
