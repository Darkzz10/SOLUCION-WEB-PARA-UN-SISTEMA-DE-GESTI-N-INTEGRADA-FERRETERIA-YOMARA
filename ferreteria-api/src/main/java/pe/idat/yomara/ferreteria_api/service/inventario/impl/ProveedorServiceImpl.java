package pe.idat.yomara.ferreteria_api.service.inventario.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.ProveedorRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ProveedorResponse;
import pe.idat.yomara.ferreteria_api.model.entity.logistica.Proveedor;
import pe.idat.yomara.ferreteria_api.repository.inventario.ProveedorRepository;
import pe.idat.yomara.ferreteria_api.service.inventario.ProveedorService;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponse> listarActivos() {
        return proveedorRepository.findByActivoTrue().stream()
                .map(this::convertirADto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponse buscarPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o inactivo"));
        return convertirADto(proveedor);
    }

    @Override
    @Transactional
    public ProveedorResponse crear(ProveedorRequest request) {
        if (proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("El RUC ya está registrado en el sistema");
        }

        Proveedor proveedor = Proveedor.builder()
                .ruc(request.getRuc())
                .razonSocial(request.getRazonSocial())
                .contacto(request.getContacto())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        return convertirADto(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional
    public ProveedorResponse actualizar(Long id, ProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o inactivo"));

        // Si cambia el RUC, verificar que no choque con otro existente
        if (!proveedor.getRuc().equals(request.getRuc()) && proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("El nuevo RUC ya pertenece a otro proveedor");
        }

        proveedor.setRuc(request.getRuc());
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setContacto(request.getContacto());
        proveedor.setEmail(request.getEmail());
        proveedor.setTelefono(request.getTelefono());

        return convertirADto(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado o ya está inactivo"));

        // BORRADO LÓGICO: Solo se cambia el estado, no hacemos un .delete()
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    // --- Mapper ---
    private ProveedorResponse convertirADto(Proveedor proveedor) {
        ProveedorResponse dto = new ProveedorResponse();
        dto.setId(proveedor.getId());
        dto.setRuc(proveedor.getRuc());
        dto.setRazonSocial(proveedor.getRazonSocial());
        dto.setContacto(proveedor.getContacto());
        dto.setEmail(proveedor.getEmail());
        dto.setTelefono(proveedor.getTelefono());
        dto.setActivo(proveedor.getActivo());
        return dto;
    }
}
