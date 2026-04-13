package pe.idat.yomara.ferreteria_api.service.inventario;

import pe.idat.yomara.ferreteria_api.model.dto.request.ProveedorRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ProveedorResponse;

import java.util.List;

public interface ProveedorService {
    List<ProveedorResponse> listarActivos();
    ProveedorResponse buscarPorId(Long id);
    ProveedorResponse crear(ProveedorRequest request);
    ProveedorResponse actualizar(Long id, ProveedorRequest request);
    void eliminar(Long id); // Borrado lógico
}
