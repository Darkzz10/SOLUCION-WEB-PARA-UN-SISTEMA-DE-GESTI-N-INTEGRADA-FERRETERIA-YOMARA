package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private String mesActual;
    private Integer totalVentasRealizadas;
    private BigDecimal ingresosTotalesMes;
    private List<ProductoTopResponse> topProductos;
}
