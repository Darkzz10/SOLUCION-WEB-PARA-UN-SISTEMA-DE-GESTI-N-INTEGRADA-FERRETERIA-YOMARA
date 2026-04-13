package pe.idat.yomara.ferreteria_api.service.inventario;

import pe.idat.yomara.ferreteria_api.model.dto.request.ProductoRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ProductoResponse;
import java.util.List;

public interface ProductoService {
    ProductoResponse crear(ProductoRequest request);
    ProductoResponse buscarPorId(Long id);
    List<ProductoResponse> listarTodos();
    List<ProductoResponse> listarActivos(); // Este es para el catálogo de ventas

    ProductoResponse actualizar(Long id, ProductoRequest request);
    List<ProductoResponse> buscarPorNombreOSku(String filtro);
    ProductoResponse buscarPorSku(String sku);
    // Método para el borrado logico
    void desactivar(Long id);
}