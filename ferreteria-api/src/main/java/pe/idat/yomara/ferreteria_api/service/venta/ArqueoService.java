package pe.idat.yomara.ferreteria_api.service.venta;

import pe.idat.yomara.ferreteria_api.model.dto.request.ArqueoRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ArqueoResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.ArqueoResumenResponse;

public interface ArqueoService {
    ArqueoResumenResponse obtenerResumenHoy(String username); // Para el GET
    ArqueoResponse ejecutarCierreSede(ArqueoRequest request, String username);
    boolean estaCerradoHoy(String username);
}
