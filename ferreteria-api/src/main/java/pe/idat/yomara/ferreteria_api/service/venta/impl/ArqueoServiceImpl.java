package pe.idat.yomara.ferreteria_api.service.venta.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.idat.yomara.ferreteria_api.model.dto.request.ArqueoRequest;
import pe.idat.yomara.ferreteria_api.model.dto.response.ArqueoResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.ArqueoResumenResponse;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Sede;
import pe.idat.yomara.ferreteria_api.model.entity.usuario.Usuario;
import pe.idat.yomara.ferreteria_api.model.entity.venta.ArqueoCaja;
import pe.idat.yomara.ferreteria_api.model.entity.venta.Venta;
import pe.idat.yomara.ferreteria_api.repository.usuario.UsuarioRepository;
import pe.idat.yomara.ferreteria_api.repository.venta.ArqueoCajaRepository;
import pe.idat.yomara.ferreteria_api.repository.venta.VentaRepository;
import pe.idat.yomara.ferreteria_api.service.venta.ArqueoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ArqueoServiceImpl implements ArqueoService {

    private final ArqueoCajaRepository arqueoCajaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;

    public ArqueoServiceImpl(ArqueoCajaRepository arqueoCajaRepository,
                             UsuarioRepository usuarioRepository,
                             VentaRepository ventaRepository) {
        this.arqueoCajaRepository = arqueoCajaRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaRepository = ventaRepository;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaCerradoHoy(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username).get();
        Sede sede = usuario.getSede();

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);
        return arqueoCajaRepository.existsBySedeIdAndFechaCierreBetween(usuario.getSede().getId(), inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public ArqueoResumenResponse obtenerResumenHoy(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username).get();
        Sede sede = usuario.getSede();

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);

        BigDecimal espEfectivo = nvl(ventaRepository.sumarTotalPorSedeYMedioPago(sede.getId(), inicio, fin, Venta.MedioPago.EFECTIVO));
        BigDecimal espTarjeta = nvl(ventaRepository.sumarTotalPorSedeYMedioPago(sede.getId(), inicio, fin, Venta.MedioPago.TARJETA));
        BigDecimal espDigital = nvl(ventaRepository.sumarTotalPorSedeYMedioPago(sede.getId(), inicio, fin, Venta.MedioPago.DIGITAL));

        return ArqueoResumenResponse.builder()
                .esperadoEfectivo(espEfectivo)
                .esperadoTarjeta(espTarjeta)
                .esperadoDigital(espDigital)
                .totalGeneral(espEfectivo.add(espTarjeta).add(espDigital))
                .sedeNombre(sede.getNombre())
                .nombreCajero(usuario.getNombreCompleto())
                .build();
    }

    @Override
    @Transactional
    public ArqueoResponse ejecutarCierreSede(ArqueoRequest request, String username) {
        if (request == null || request.getMontoReal() == null) {
            throw new RuntimeException("El monto real es obligatorio para cerrar la caja.");
        }

        Usuario usuario = usuarioRepository.findByUsername(username).get();
        Sede sede = usuario.getSede();

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);

        // VALIDACIÓN: Evitar duplicidad de arqueo por sede
        if (arqueoCajaRepository.existsBySedeIdAndFechaCierreBetween(sede.getId(), inicio, fin)) {
            throw new RuntimeException("La caja de la sede " + sede.getNombre() + " ya fue cerrada el día de hoy.");
        }

        if (request.getMontoReal().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El monto real reportado no puede ser un valor negativo.");
        }

        BigDecimal espEfectivo = nvl(ventaRepository.sumarTotalPorSedeYMedioPago(sede.getId(), inicio, fin, Venta.MedioPago.EFECTIVO));
        BigDecimal espTarjeta = nvl(ventaRepository.sumarTotalPorSedeYMedioPago(sede.getId(), inicio, fin, Venta.MedioPago.TARJETA));
        BigDecimal espDigital = nvl(ventaRepository.sumarTotalPorSedeYMedioPago(sede.getId(), inicio, fin, Venta.MedioPago.DIGITAL));

        BigDecimal totalEsperado = espEfectivo.add(espTarjeta).add(espDigital);
        BigDecimal diferencia = request.getMontoReal().subtract(totalEsperado);

        ArqueoCaja arqueo = ArqueoCaja.builder()
                .fechaCierre(LocalDateTime.now())
                .montoEsperado(totalEsperado)
                .montoReal(request.getMontoReal())
                .diferencia(diferencia)
                .observaciones(request.getObservaciones() != null ? request.getObservaciones() : "Sin observaciones")
                .usuario(usuario)
                .sede(sede)
                .build();

        return convertirADto(arqueoCajaRepository.save(arqueo));
    }

    private ArqueoResponse convertirADto(ArqueoCaja arqueo) {
        ArqueoResponse res = new ArqueoResponse();
        res.setId(arqueo.getId());
        res.setFechaCierre(arqueo.getFechaCierre());
        res.setMontoEsperado(arqueo.getMontoEsperado());
        res.setMontoReal(arqueo.getMontoReal());
        res.setDiferencia(arqueo.getDiferencia());
        res.setObservaciones(arqueo.getObservaciones());
        res.setVendedorNombre(arqueo.getUsuario().getNombreCompleto());
        res.setSedeNombre(arqueo.getSede().getNombre());
        return res;
    }
}