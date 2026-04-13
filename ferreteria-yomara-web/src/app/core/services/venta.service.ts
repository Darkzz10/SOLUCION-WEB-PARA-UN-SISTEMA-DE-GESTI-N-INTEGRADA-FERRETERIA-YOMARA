import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { VentaResponse } from '../../data/interfaces/venta-response';

@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/ventas`;

  // Listar para el historial
  listarVentas(): Observable<VentaResponse[]> {
    return this.http.get<VentaResponse[]>(this.apiUrl);
  }

  //Registrar la venta en la base de datos
  registrarVenta(ventaData: any): Observable<VentaResponse> {
    return this.http.post<VentaResponse>(this.apiUrl, ventaData);
  }

  // Descargar el PDF
  descargarPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/comprobante`, { responseType: 'blob' });
  }
}