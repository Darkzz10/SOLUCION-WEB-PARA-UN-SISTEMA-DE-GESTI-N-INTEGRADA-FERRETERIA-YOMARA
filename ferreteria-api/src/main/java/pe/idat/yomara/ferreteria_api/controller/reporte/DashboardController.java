package pe.idat.yomara.ferreteria_api.controller.reporte;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.idat.yomara.ferreteria_api.model.dto.response.DashboardResponse;
import pe.idat.yomara.ferreteria_api.service.reporte.ReporteService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ReporteService reporteService;

    public DashboardController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mensual")
    public ResponseEntity<DashboardResponse> verDashboardMensual() {
        return ResponseEntity.ok(reporteService.obtenerDashboardDelMes());
    }
}
