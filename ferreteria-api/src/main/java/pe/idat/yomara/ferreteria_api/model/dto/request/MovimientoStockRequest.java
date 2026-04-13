package pe.idat.yomara.ferreteria_api.model.dto.request;

import lombok.Data;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;

@Data
public class MovimientoStockRequest {
    private Long productoId;
    private Long sedeId;
    private Integer cantidad;
    private TipoMovimiento tipoMovimiento;
    private String motivo;
}
