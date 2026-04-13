package pe.idat.yomara.ferreteria_api.service.usuario.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.SedeRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.SedeResponse;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;
import pe.idat.yomara.ferreteria_api.repository.usuario.SedeRepository;
import pe.idat.yomara.ferreteria_api.service.usuario.SedeService;

import java.util.List;

@Service
public class SedeServiceImpl implements SedeService {

    private final SedeRepository sedeRepository;

    public SedeServiceImpl(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    @Override
    @Transactional
    public SedeResponse crear(SedeRequest request) {
        // 1. VALIDACIÓN: Campos obligatorios
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la sede es obligatorio.");
        }

        // 2. VALIDACIÓN: Evitar duplicados por nombre
        if (sedeRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una sede registrada con el nombre: " + request.getNombre());
        }

        Sede sede = Sede.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .build();

        return convertirADto(sedeRepository.save(sede));
    }

    @Override
    @Transactional(readOnly = true)
    public SedeResponse buscarPorId(Long id) {
        // 3. VALIDACIÓN: Buscar y lanzar error limpio si no existe
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con el ID: " + id));
        return convertirADto(sede);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SedeResponse> listarTodas() {
        return sedeRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    private SedeResponse convertirADto(Sede sede) {
        SedeResponse res = new SedeResponse();
        res.setId(sede.getId());
        res.setNombre(sede.getNombre());
        res.setDireccion(sede.getDireccion());
        res.setTelefono(sede.getTelefono());
        return res;
    }
}