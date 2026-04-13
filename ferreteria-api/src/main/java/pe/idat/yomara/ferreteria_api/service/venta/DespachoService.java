package pe.idat.yomara.ferreteria_api.service.venta;

import pe.idat.yomara.ferreteria_api.model.dto.response.DespachoResponse;
import java.util.List;

public interface DespachoService {
    List<DespachoResponse> obtenerHojaDeRuta(Long sedeId);
    void actualizarEstado(Long ventaId, String nuevoEstado, String evidencia);
}
