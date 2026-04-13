package pe.idat.yomara.ferreteria_api.controller.venta;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.VentaRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.VentaResponse;
import pe.idat.yomara.ferreteria_api.service.venta.VentaService;
import java.util.List;

import java.security.Principal;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    // Solo VENDEDOR y ADMIN pueden facturar. (El ALMACENERO es bloqueado aquí).
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @PostMapping
    public ResponseEntity<VentaResponse> registrarVenta(
            @RequestBody VentaRequest request,
            Principal principal) {

        // El principal.getName() manda el username al Service para la lógica Zero Trust
        return new ResponseEntity<>(ventaService.registrarVenta(request, principal.getName()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/{id}/comprobante")
    public ResponseEntity<byte[]> descargarComprobantePdf(@PathVariable("id") Long id) {

        byte[] pdfBytes = ventaService.generarComprobantePdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Le indicamos al navegador el nombre del archivo a descargar
        headers.setContentDispositionFormData("attachment", "Comprobante_Venta_" + id + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
