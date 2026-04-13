package pe.idat.yomara.ferreteria_api.service.venta.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.response.DespachoResponse;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Venta;
import pe.idat.yomara.ferreteria_api.model.entity.venta.EstadoVenta;
import pe.idat.yomara.ferreteria_api.repository.venta.VentaRepository;
import pe.idat.yomara.ferreteria_api.service.venta.DespachoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespachoServiceImpl implements DespachoService {

    private final VentaRepository ventaRepository;

    public DespachoServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DespachoResponse> obtenerHojaDeRuta(Long sedeId) {
        List<Venta> ventas = ventaRepository.findBySedeIdAndEstadoVenta(sedeId, EstadoVenta.PENDIENTE_DESPACHO);

        return ventas.stream()
                .map(venta -> mapToDespachoResponse(venta))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarEstado(Long ventaId, String nuevoEstado, String evidencia) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        EstadoVenta proximoEstado = EstadoVenta.valueOf(nuevoEstado);
        venta.setEstadoVenta(proximoEstado);

        // Si el transportista confirma la entrega, guardamos la foto/firma (evidencia)
        if (proximoEstado == EstadoVenta.ENTREGADO && evidencia != null) {
            venta.setEvidenciaEntrega(evidencia);
        }

        ventaRepository.save(venta);
    }

    // 💡 ESTA ES LA UBICACIÓN CORRECTA DEL MÉTODO HELPER
    private DespachoResponse mapToDespachoResponse(Venta v) {
        return DespachoResponse.builder()
                .ventaId(v.getId())
                .clienteNombre(v.getCliente().getNombreCompleto())
                .clienteTelefono(v.getCliente().getTelefono())
                .direccionEntrega(v.getDireccionEntrega())
                .montoTotal(v.getTotal())
                .estado(v.getEstadoVenta().name())
                // Usamos el null-check para evitar errores si la fecha es nula
                .fechaVenta(v.getFechaVenta() != null ? v.getFechaVenta().toString() : "")
                .build();
    }
}