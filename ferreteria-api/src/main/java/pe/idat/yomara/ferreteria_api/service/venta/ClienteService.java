package pe.idat.yomara.ferreteria_api.service.venta;

import org.springframework.data.domain.Page;
import pe.idat.yomara.ferreteria_api.model.dto.request.ClienteRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ClienteResponse;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest request);

    Page<ClienteResponse> listarPaginado(int page, int size, String nombre);

    ClienteResponse buscarPorDocumento(String nro);

    ClienteResponse actualizar(Long id, ClienteRequest request);
    void desactivar(Long id); // Borrado Lógico
}
