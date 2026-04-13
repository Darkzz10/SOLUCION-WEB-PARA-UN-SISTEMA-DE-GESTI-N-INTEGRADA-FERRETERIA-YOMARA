import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse } from '../../data/interfaces/auth.interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  
  // Como ya arreglamos el environment.development.ts, esto funcionará perfecto
  private authUrl = `${environment.apiUrl}/auth`;

  constructor() { }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.authUrl}/login`, credentials).pipe(
      tap((res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', res.username);
        localStorage.setItem('sedeNombre', res.sedeNombre);

        // CONVERSIÓN :
        // Tomamos el 'rol' (String) y lo pasamos a un Array ['ADMIN']
        // Así no se rompe el getRoles() ni el hasAnyRole()
        const rolesArray = [res.rol]; 
        localStorage.setItem('roles', JSON.stringify(rolesArray));
        
        console.log('Login exitoso. Rol detectado:', res.rol);
      })
    );
  }

  // --- MÉTODOS ---

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  getRoles(): string[] {
    const roles = localStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  hasAnyRole(expectedRoles: string | string[]): boolean {
    const userRoles = this.getRoles();
    if (Array.isArray(expectedRoles)) {
      return userRoles.some(role => expectedRoles.includes(role));
    }
    return userRoles.includes(expectedRoles);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.clear();
  }
}