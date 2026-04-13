package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlertaStockResponse {
    private Long productoId;
    private String nombreProducto;
    private String sku;
    private Integer stockActualTotal; // la suma de todas las sedes
    private Integer stockMinimoRequerido;
    private Boolean alertaRoja; // para el Frontend
}
