package pe.idat.yomara.ferreteria_api.controller.logistica;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.yomara.ferreteria_api.model.dto.request.CategoriaRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.CategoriaResponse;
import pe.idat.yomara.ferreteria_api.service.inventario.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")

// @CrossOrigin(origins = "*") // Descomenta esto después para que Angular no llore por CORS

public class CategoriaController {

    private final CategoriaService categoriaService;

    // Inyección por constructor para pruebas con mockito
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET http://localhost:8080/api/categorias
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarSoloActivas() {
        return new ResponseEntity<>(categoriaService.listarSoloActivas(), HttpStatus.OK);
    }

    // POST http://localhost:8080/api/categorias
    @PostMapping
    public ResponseEntity<CategoriaResponse> guardar(@Valid @RequestBody CategoriaRequest request) {
        System.out.println("LOG - Nombre recibido: " + request.getNombre());
        System.out.println("LOG - Descripción recibida: " + request.getDescripcion());

        CategoriaResponse nuevaCategoria = categoriaService.guardar(request);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
            @Valid@PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse response = categoriaService.actualizar(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activar(@Valid @PathVariable Long id) {
        categoriaService.activar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@Valid @PathVariable Long id) {
        categoriaService.desactivar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}