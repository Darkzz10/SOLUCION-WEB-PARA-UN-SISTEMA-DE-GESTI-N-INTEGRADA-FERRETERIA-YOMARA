package pe.idat.yomara.ferreteria_api.service.inventario;

import pe.idat.yomara.ferreteria_api.model.dto.request.*;
import pe.idat.yomara.ferreteria_api.model.dto.response.*;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponse> listarTodas(); // Devuelve una lista de DTOs
    CategoriaResponse guardar(CategoriaRequest request); // Recibe Request, devuelve Response
    List<CategoriaResponse> listarSoloActivas();
    CategoriaResponse actualizar(Long id, CategoriaRequest request);
    void desactivar(Long id);
    void activar(Long id); // Para volver a poner activo = true
}
