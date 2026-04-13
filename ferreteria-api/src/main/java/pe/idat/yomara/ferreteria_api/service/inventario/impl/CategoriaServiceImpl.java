package pe.idat.yomara.ferreteria_api.service.inventario.impl;

import org.springframework.stereotype.Service;
import pe.idat.yomara.ferreteria_api.model.dto.request.CategoriaRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.CategoriaResponse;
import pe.idat.yomara.ferreteria_api.model.entity.inventario.Categoria;
import pe.idat.yomara.ferreteria_api.repository.inventario.CategoriaRepository;
import pe.idat.yomara.ferreteria_api.service.inventario.CategoriaService;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }


    @Override
    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    @Override
    public List<CategoriaResponse> listarSoloActivas() {
        return categoriaRepository.findAll().stream()
                .filter(entidad -> entidad.getActivo() != null && entidad.getActivo())
                .map(this::convertirADto)
                .toList();
    }

    @Override
    public CategoriaResponse guardar(CategoriaRequest request) {

        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una categoria con el nombre: " + request.getNombre());
        }

        // Convertimos Request -> Entidad
        Categoria entidad = new Categoria();
        entidad.setNombre(request.getNombre());
        entidad.setDescripcion(request.getDescripcion());
        entidad.setActivo(true);

        return convertirADto(categoriaRepository.save(entidad));
    }

    @Override
    public void desactivar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }

    @Override
    public void activar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setActivo(true); // Cambiamos el switch a encendido
        categoriaRepository.save(categoria);
    }


    @Override
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        // Buscamos si existe la categoría en la base de datos
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Categoría no encontrada"));

        // Seteamos los nuevos valores que vienen en el Request
        categoriaExistente.setNombre(request.getNombre());
        categoriaExistente.setDescripcion(request.getDescripcion());

        // Guardamos los cambios (JPA detecta que el ID ya existe y hace un UPDATE en lugar de un INSERT)
        Categoria entidadActualizada = categoriaRepository.save(categoriaExistente);

        // Devolvemos el Response DTO usando nuestro método ayudante
        return convertirADto(entidadActualizada);
    }

    // traducción de Entidad a Response DTO
    private CategoriaResponse convertirADto(Categoria entidad) {
        CategoriaResponse dto = new CategoriaResponse();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setActivo(entidad.getActivo());
        return dto;
    }
}