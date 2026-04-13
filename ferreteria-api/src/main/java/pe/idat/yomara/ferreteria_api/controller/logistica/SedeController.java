package pe.idat.yomara.ferreteria_api.controller.logistica;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.SedeRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.SedeResponse;
import pe.idat.yomara.ferreteria_api.service.usuario.SedeService;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @PostMapping
    public ResponseEntity<SedeResponse> crear(@RequestBody SedeRequest request) {
        return new ResponseEntity<>(sedeService.crear(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SedeResponse>> listar() {
        return ResponseEntity.ok(sedeService.listarTodas());
    }
}
