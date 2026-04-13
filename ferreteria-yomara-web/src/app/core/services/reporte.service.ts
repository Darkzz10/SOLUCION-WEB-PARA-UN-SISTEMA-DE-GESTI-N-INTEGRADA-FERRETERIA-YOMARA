import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { DashboardResponse } from '../../data/interfaces/dashboard-response';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/dashboard`;

  constructor() { }

  getDashboardMes(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/mensual`);
  }
}