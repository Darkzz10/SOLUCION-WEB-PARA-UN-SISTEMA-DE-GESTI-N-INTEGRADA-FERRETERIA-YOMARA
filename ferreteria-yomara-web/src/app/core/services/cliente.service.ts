import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

// DTOs basados en tus archivos Java
export interface ClienteRequest {
  documentoNumero: string;
  nombreCompleto: string;
  email?: string;
  telefono?: string;
}

export interface ClienteResponse {
  id: number;
  documentoNumero: string;
  nombreCompleto: string;
  email: string;
  telefono: string;
  activo: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/clientes`;

    listarClientes(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  // Retorna el JSON paginado buscando por DNI/RUC
  buscarPorDocumento(documento: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/documento/${documento}`);
  }

  registrarCliente(cliente: ClienteRequest): Observable<ClienteResponse> {
    return this.http.post<ClienteResponse>(this.apiUrl, cliente);
  }

  actualizarCliente(id: number, cliente: ClienteRequest): Observable<ClienteResponse> {
    return this.http.put<ClienteResponse>(`${this.apiUrl}/${id}`, cliente);
  }
}