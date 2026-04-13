package pe.idat.yomara.ferreteria_api.service.venta;

import pe.idat.yomara.ferreteria_api.model.dto.request.VentaRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.VentaResponse;

import java.util.List;

public interface VentaService {
    VentaResponse registrarVenta(VentaRequest request, String usernameAutenticado);
    List<VentaResponse> listarTodas();
    VentaResponse buscarPorId(Long id);
    byte[] generarComprobantePdf(Long idVenta);
}
