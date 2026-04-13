import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReporteService } from '../../../core/services/reporte.service';
import { DashboardResponse, VentasPorSede, ProductoTop } from '../../../data/interfaces/dashboard-response';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule], // 👈 Quitamos MatProgressBarModule de aquí
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  private reporteService = inject(ReporteService);

  dashboardData?: DashboardResponse;
  isLoading = true;
  nombreSedeActual = 'Yomara - Central';

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.reporteService.getDashboardMes().subscribe({
      next: (data) => {
        this.dashboardData = data;
        this.dashboardData.ventasPorSede = this.getMockVentasPorSede();
        this.dashboardData.productosTop = this.getMockProductosTop();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar dashboard:', err);
        this.isLoading = false;
        this.dashboardData = { totalVentasMes: 0 };
        this.dashboardData.ventasPorSede = this.getMockVentasPorSede();
        this.dashboardData.productosTop = this.getMockProductosTop();
      }
    });
  }

  private getMockVentasPorSede(): VentasPorSede[] {
    return [
      { sede: 'Yomara - Central', monto: 8500, porcentaje: 100 },
      { sede: 'Yomara - Sur', monto: 4200, porcentaje: 50 },
      { sede: 'Yomara - Norte', monto: 2500, porcentaje: 30 }
    ];
  }

  private getMockProductosTop(): ProductoTop[] {
    return [
      { nombre: 'Martillo Ind. Tramontina', cantidad: 120, ingresoTotal: 3000, porcentaje: 100 },
      { nombre: 'Taladro Percutor Bosch', cantidad: 45, ingresoTotal: 9000, porcentaje: 70 },
      { nombre: 'Caja de Clavos 2"', cantidad: 500, ingresoTotal: 1000, porcentaje: 90 },
      { nombre: 'Pintura Látex Vencedor', cantidad: 80, ingresoTotal: 5600, porcentaje: 60 },
      { nombre: 'Set de Destornilladores', cantidad: 95, ingresoTotal: 1900, porcentaje: 50 }
    ];
  }
}