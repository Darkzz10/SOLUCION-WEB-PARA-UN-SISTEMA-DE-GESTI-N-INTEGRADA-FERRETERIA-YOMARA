import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VentaService } from '../../../core/services/venta.service';
import { VentaResponse } from '../../../data/interfaces/venta-response';

@Component({
  selector: 'app-historial-ventas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './historial-ventas.component.html'
})
export class HistorialVentasComponent implements OnInit {
  private ventaService = inject(VentaService);

  ventas: VentaResponse[] = []; 
  cargando = true;

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas() {
    this.cargando = true;
    this.ventaService.listarVentas().subscribe({
      next: (res) => {
        this.ventas = res;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar historial:', err);
        this.cargando = false;
      }
    });
  }

  imprimirComprobante(id: number) {
    this.ventaService.descargarPdf(id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const nuevoTab = window.open(url, '_blank');
        
        if (nuevoTab) {
          nuevoTab.focus();
        }
      },
      error: (err) => {
        console.error('Error al generar el PDF:', err);
        alert('No se pudo generar el comprobante en este momento.');
      }
    });
  }

  getEstadoClase(estado: string): string {
    const s = estado ? estado.toUpperCase() : '';
    if (s.includes('EMITIDO') || s.includes('PAGADO')) return 'bg-green-100 text-green-700 border border-green-200';
    if (s.includes('ANULADO')) return 'bg-red-100 text-red-700 border border-red-200';
    if (s.includes('PENDIENTE')) return 'bg-amber-100 text-amber-700 border border-amber-200';
    return 'bg-slate-100 text-slate-500 border border-slate-200';
  }
}