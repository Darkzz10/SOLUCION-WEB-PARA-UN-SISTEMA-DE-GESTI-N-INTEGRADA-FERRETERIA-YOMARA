package pe.idat.yomara.ferreteria_api.controller.logistica;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.MovimientoStockRequest;
import pe.idat.yomara.ferreteria_api.model.dto.request.TransferenciaStockRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.AlertaStockResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.MovimientoResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.StockGlobalResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.TipoMovimiento;
import pe.idat.yomara.ferreteria_api.service.inventario.InventarioService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    //  Cualquiera autenticado puede consultar stock (Admin, Almacenero, Vendedor)
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<StockGlobalResponse> consultarStock(
            @PathVariable Long productoId,
            Principal principal) {
        return ResponseEntity.ok(inventarioService.consultarStockPriorizado(productoId, principal.getName()));
    }

    // Solo ADMIN y ALMACENERO pueden registrar movimientos de mercadería
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @PostMapping("/movimiento")
    public ResponseEntity<String> registrarMovimiento(
            @RequestBody MovimientoStockRequest request,
            Principal principal) {
        inventarioService.registrarMovimiento(request, principal.getName());
        return ResponseEntity.ok("Movimiento registrado con éxito. Stock actualizado.");
    }

    // Endpoint exclusivo para el Dashboard Gerencial
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alertas-stock")
    public ResponseEntity<List<AlertaStockResponse>> verAlertasDeStock() {
        return ResponseEntity.ok(inventarioService.obtenerAlertasDeStock());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @PostMapping("/traslado")
    public ResponseEntity<String> trasladarStock(
            @RequestBody TransferenciaStockRequest request,
            Principal principal) {
        inventarioService.trasladarStock(request, principal.getName());
        return ResponseEntity.ok("Traslado entre sedes procesado correctamente.");
    }

    // Endpoint para el Kardex con filtros
    @GetMapping("/kardex")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    public ResponseEntity<List<MovimientoResponse>> verKardex(
            @RequestParam(required = false) Long sedeId,
            @RequestParam(required = false) TipoMovimiento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(inventarioService.obtenerKardex(sedeId, tipo, inicio, fin));
    }

    // Endpoint para anular
    @PostMapping("/movimientos/{id}/anular")
    @PreAuthorize("hasRole('ADMIN')") // Solo el Admin debería poder anular
    public ResponseEntity<String> anular(@PathVariable Long id, @RequestBody String motivo, Principal principal) {
        inventarioService.anularMovimiento(id, motivo, principal.getName());
        return ResponseEntity.ok("Movimiento anulado correctamente y stock revertido.");
    }
}
