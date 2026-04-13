import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service'; // Ajusta la ruta si es necesario

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  // 1. Inyectamos los servicios necesarios
  private authService = inject(AuthService);
  private router = inject(Router);

  // 2. Modelo de datos para el formulario
  loginData = {
    username: '',
    password: ''
  };

  errorMessage: string | null = null;
  isLoading = false;

  onLogin() {
    if (!this.loginData.username || !this.loginData.password) return;

    this.isLoading = true;
    this.errorMessage = null;

    this.authService.login(this.loginData).subscribe({
      next: (res) => {
        console.log('Login exitoso:', res.username);
        // Redirigimos al contenedor principal
        // Como 'dashboard' es hijo de 'main', la ruta es '/main/dashboard'
        this.router.navigate(['/main/dashboard']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Usuario o contraseña incorrectos';
        console.error('Error en login:', err);
      }
    });
  }
}