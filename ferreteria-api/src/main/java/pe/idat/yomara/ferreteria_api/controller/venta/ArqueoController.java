package pe.idat.yomara.ferreteria_api.controller.venta;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.ArqueoRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ArqueoResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.ArqueoResumenResponse;
import pe.idat.yomara.ferreteria_api.service.venta.ArqueoService;

import java.security.Principal;

@RestController
@RequestMapping("/api/arqueos")
public class ArqueoController {

    private final ArqueoService arqueoService;

    public ArqueoController(ArqueoService arqueoService) {
        this.arqueoService = arqueoService;
    }

    @GetMapping("/resumen-esperado")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ArqueoResumenResponse> obtenerResumen(Principal principal) {
        return ResponseEntity.ok(arqueoService.obtenerResumenHoy(principal.getName()));
    }

    @GetMapping("/estado-hoy")
    public ResponseEntity<Boolean> verificarEstadoCaja(Principal principal) {
        // 💡 Ahora sí devuelve la verdad desde la BD
        return ResponseEntity.ok(arqueoService.estaCerradoHoy(principal.getName()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ArqueoResponse> realizarArqueo(@RequestBody ArqueoRequest request, Principal principal) {
        return ResponseEntity.ok(arqueoService.ejecutarCierreSede(request, principal.getName()));
    }
}
