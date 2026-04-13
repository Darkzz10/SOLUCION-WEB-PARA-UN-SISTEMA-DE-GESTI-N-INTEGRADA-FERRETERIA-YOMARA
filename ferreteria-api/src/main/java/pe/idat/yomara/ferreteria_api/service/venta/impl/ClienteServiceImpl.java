package pe.idat.yomara.ferreteria_api.service.venta.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.idat.yomara.ferreteria_api.model.dto.request.ClienteRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ClienteResponse;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Cliente;
import pe.idat.yomara.ferreteria_api.repository.venta.ClienteRepository;
import pe.idat.yomara.ferreteria_api.service.venta.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponse crear(ClienteRequest request) {
        if(clienteRepository.findByDocumentoNumero(request.getDocumentoNumero()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con ese número de documento.");
        }

        Cliente cliente = Cliente.builder()
                .documentoNumero(request.getDocumentoNumero())
                .nombreCompleto(request.getNombreCompleto())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        return convertirADto(clienteRepository.save(cliente));
    }

    @Override
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Actualizamos los datos (Ignoramos el DNI/RUC para mantener la integridad)
        cliente.setNombreCompleto(request.getNombreCompleto());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());

        return convertirADto(clienteRepository.save(cliente));
    }

    @Override
    public void desactivar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Borrado Lógico: Solo le apagamos el switch
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    public ClienteResponse buscarPorDocumento(String nro) {
        return clienteRepository.findByDocumentoNumero(nro)
                .map(this::convertirADto)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
    }

    @Override
    public Page<ClienteResponse> listarPaginado(int page, int size, String nombre) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombreCompleto").ascending());

        // Si el nombre es nulo o vacío, listamos todos los paginados
        if (nombre == null || nombre.trim().isEmpty()) {
            return clienteRepository.findAll(pageable).map(this::convertirADto);
        }

        // Si hay nombre, filtramos
        return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre, pageable)
                .map(this::convertirADto);
    }

    private ClienteResponse convertirADto(Cliente cliente) {
        ClienteResponse res = new ClienteResponse();
        res.setId(cliente.getId());
        res.setDocumentoNumero(cliente.getDocumentoNumero());
        res.setNombreCompleto(cliente.getNombreCompleto());
        res.setEmail(cliente.getEmail());
        res.setTelefono(cliente.getTelefono());
        res.setActivo(cliente.getActivo());
        return res;
    }
}

