package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class StockGlobalResponse {
    private String nombreProducto;
    private String sku;
    private Integer totalGlobal;
    private List<DetalleStockSede> existencias;
}
