package pe.idat.yomara.ferreteria_api.controller.venta;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.ClienteRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ClienteResponse;
import pe.idat.yomara.ferreteria_api.service.venta.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@RequestBody ClienteRequest request) {
        return new ResponseEntity<>(clienteService.crear(request), HttpStatus.CREATED);
    }

    // Endpoint paginado: /api/clientes?page=0&size=10&nombre=juan
    @GetMapping
    public ResponseEntity<Page<ClienteResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(clienteService.listarPaginado(page, size, nombre));
    }

    // Endpoint para editar: PUT /api/clientes/5
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }

    // Endpoint para desactivar: DELETE /api/clientes/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivar(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content (Estándar REST)
    }

    @GetMapping("/documento/{nro}")
    public ResponseEntity<ClienteResponse> buscarPorDoc(@PathVariable String nro) {
        return ResponseEntity.ok(clienteService.buscarPorDocumento(nro));
    }
}
