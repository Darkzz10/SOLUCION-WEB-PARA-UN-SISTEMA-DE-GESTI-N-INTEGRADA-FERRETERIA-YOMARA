import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VentaService } from '../../../core/services/venta.service';
import { VentaResponse } from '../../../data/interfaces/venta-response';

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedidos.component.html',
})
export class PedidosComponent implements OnInit {
  private ventaService = inject(VentaService);

  ventas: VentaResponse[] = [];
  isLoading = true;

  ngOnInit(): void {
    this.obtenerHistorialVentas();
  }

  obtenerHistorialVentas(): void {
    this.isLoading = true;
    this.ventaService.listarVentas().subscribe({
      next: (data) => {
        this.ventas = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al traer ventas:', err);
        this.isLoading = false;
      }
    });
  }

  imprimir(id: number): void {
    this.ventaService.descargarPdf(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Comprobante_Venta_${id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al descargar el comprobante:', err);
        alert('No se pudo generar el PDF en este momento.');
      }
    });
  }
}