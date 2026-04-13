package pe.idat.yomara.ferreteria_api.service.inventario;

import pe.idat.yomara.ferreteria_api.model.dto.request.MovimientoStockRequest;
import pe.idat.yomara.ferreteria_api.model.dto.request.TransferenciaStockRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.AlertaStockResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.MovimientoResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.StockGlobalResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;

import java.time.LocalDate;
import java.util.List;

public interface InventarioService {
    StockGlobalResponse consultarStockPriorizado(Long productoId, String usernameAutenticado);
    void registrarMovimiento(MovimientoStockRequest request, String usernameAutenticado);
    List<AlertaStockResponse> obtenerAlertasDeStock();
    void trasladarStock(TransferenciaStockRequest request, String usernameAutenticado);
    List<MovimientoResponse> obtenerKardex(Long sedeId, TipoMovimiento tipo, LocalDate inicio, LocalDate fin);
    void anularMovimiento(Long movimientoId, String motivo, String usernameAutenticado);
}