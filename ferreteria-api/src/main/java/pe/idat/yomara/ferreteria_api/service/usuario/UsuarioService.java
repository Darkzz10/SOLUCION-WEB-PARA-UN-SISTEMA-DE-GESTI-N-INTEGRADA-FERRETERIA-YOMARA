package pe.idat.yomara.ferreteria_api.service.usuario;


import pe.idat.yomara.ferreteria_api.model.dto.request.UsuarioRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.UsuarioDetalleResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioRequest request);
    UsuarioResponse buscarPorId(Long id);
    List<UsuarioResponse> listarTodos();
    List<UsuarioDetalleResponse> listarColaboradores();
    void desactivarUsuario(Long id);
}
