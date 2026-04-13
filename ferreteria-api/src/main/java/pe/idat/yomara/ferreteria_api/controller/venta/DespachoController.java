package pe.idat.yomara.ferreteria_api.controller.venta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.response.DespachoResponse;
import pe.idat.yomara.ferreteria_api.service.venta.DespachoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/despachos")
public class DespachoController {

    private final DespachoService despachoService;

    public DespachoController(DespachoService despachoService) {
        this.despachoService = despachoService;
    }

    // El transportista consulta su lista: GET /api/despachos?sedeId=1
    @GetMapping
    public ResponseEntity<List<DespachoResponse>> listarHojaDeRuta(@RequestParam Long sedeId) {
        return ResponseEntity.ok(despachoService.obtenerHojaDeRuta(sedeId));
    }

    // El transportista cambia estado: PATCH /api/despachos/10/estado
    // Body: { "nuevoEstado": "EN_RUTA", "evidencia": "base64string..." }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        String nuevoEstado = payload.get("nuevoEstado");
        String evidencia = payload.get("evidencia");

        despachoService.actualizarEstado(id, nuevoEstado, evidencia);
        return ResponseEntity.ok("Estado de despacho actualizado a: " + nuevoEstado);
    }
}
