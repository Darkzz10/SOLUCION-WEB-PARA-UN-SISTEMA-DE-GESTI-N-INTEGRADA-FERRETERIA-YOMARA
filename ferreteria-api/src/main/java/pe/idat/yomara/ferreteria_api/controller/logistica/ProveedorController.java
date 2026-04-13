package pe.idat.yomara.ferreteria_api.controller.logistica;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.ProveedorRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ProveedorResponse;
import pe.idat.yomara.ferreteria_api.service.inventario.ProveedorService;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> listar() {
        return ResponseEntity.ok(proveedorService.listarActivos());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @PostMapping
    public ResponseEntity<ProveedorResponse> crear(@Valid @RequestBody ProveedorRequest request) {
        return new ResponseEntity<>(proveedorService.crear(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENERO')")
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.actualizar(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')") // Solo el ADMIN debería poder "eliminar" un proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}