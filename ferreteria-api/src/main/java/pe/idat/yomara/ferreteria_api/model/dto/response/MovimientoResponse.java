package pe.idat.yomara.ferreteria_api.model.dto.response;

import lombok.Data;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;
import java.time.LocalDateTime;

@Data
public class MovimientoResponse {
    private Long id;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private String motivo;
    private String productoNombre;
    private String sedeNombre;
    private String usuarioNombre;
    private boolean anulado;
}
