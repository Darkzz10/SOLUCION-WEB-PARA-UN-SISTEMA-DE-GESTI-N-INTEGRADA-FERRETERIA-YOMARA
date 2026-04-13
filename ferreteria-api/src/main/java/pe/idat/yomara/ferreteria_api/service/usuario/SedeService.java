package pe.idat.yomara.ferreteria_api.service.usuario;

import pe.idat.yomara.ferreteria_api.model.dto.request.SedeRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.SedeResponse;

import java.util.List;

public interface SedeService {

    SedeResponse crear(SedeRequest request);
    SedeResponse buscarPorId(Long id);
    List<SedeResponse> listarTodas();

}
