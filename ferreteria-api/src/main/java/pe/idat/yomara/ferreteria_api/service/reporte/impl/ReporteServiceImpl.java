package pe.idat.yomara.ferreteria_api.service.reporte.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.idat.yomara.ferreteria_api.model.dto.response.DashboardResponse;
import pe.idat.yomara.ferreteria_api.model.dto.response.ProductoTopResponse;
import pe.idat.yomara.ferreteria_api.repository.venta.VentaRepository;
import pe.idat.yomara.ferreteria_api.service.reporte.ReporteService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;

    public ReporteServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public DashboardResponse obtenerDashboardDelMes() {

        // 1. Calcular el primer y último día del mes actual (Ej: 1 de Marzo al 31 de Marzo)
        YearMonth mesActual = YearMonth.now();
        LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
        LocalDateTime finMes = mesActual.atEndOfMonth().atTime(23, 59, 59);

        // 2. Ejecutar consultas ultrarrápidas
        BigDecimal ingresos = ventaRepository.calcularIngresosDelMes(inicioMes, finMes);
        if (ingresos == null) ingresos = BigDecimal.ZERO; // Por si no hay ventas aún

        Integer totalVentas = ventaRepository.contarVentasDelMes(inicioMes, finMes);

        // 3. Obtener el Top 5 de productos (usamos PageRequest para limitar a 5)
        Pageable top5 = PageRequest.of(0, 5);
        List<Object[]> resultadosTop = ventaRepository.obtenerTopProductosDelMes(inicioMes, finMes, top5);

        // Mapear el resultado Object[] a nuestro DTO
        List<ProductoTopResponse> topProductos = resultadosTop.stream().map(obj -> {
            String nombre = (String) obj[0];
            Long cantidad = ((Number) obj[1]).longValue();
            BigDecimal ingresoTotal = (BigDecimal) obj[2];
            return new ProductoTopResponse(nombre, cantidad, ingresoTotal);
        }).collect(Collectors.toList());

        // 4. Nombre del mes en español (Ej: "Marzo 2026")
        String nombreMes = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String mesFormateado = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1) + " " + mesActual.getYear();

        // 5. Armar y retornar el Dashboard
        return DashboardResponse.builder()
                .mesActual(mesFormateado)
                .totalVentasRealizadas(totalVentas)
                .ingresosTotalesMes(ingresos)
                .topProductos(topProductos)
                .build();
    }
}
