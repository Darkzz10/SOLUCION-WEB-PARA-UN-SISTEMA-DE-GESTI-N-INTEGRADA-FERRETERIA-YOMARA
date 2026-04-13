import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Obtenemos el rol o roles permitidos para esta ruta desde app.routes.ts
  const expectedRoles = route.data['role'] as string[];

  // Verificamos si el usuario tiene al menos uno de esos roles
  if (authService.isAuthenticated() && authService.hasAnyRole(expectedRoles)) {
    return true;
  }

  // Si no tiene permiso, lo mandamos al login o a una página de "Denegado"
  alert('No tienes permisos para acceder aquí.');
  router.navigate(['/login']);
  return false;
};
