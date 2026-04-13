import { Routes } from '@angular/router';
import { LoginComponent } from './modules/auth/login/login.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { InventarioComponent } from './modules/inventario/inventario.component';
import { VentasComponent } from './modules/ventas/ventas.component';
import { HistorialVentasComponent } from './modules/ventas/historial-ventas/historial-ventas.component';
import { ClientesComponent } from './modules/clientes/clientes.component';
import { DashboardComponent } from './modules/admin/dashboard/dashboard.component';
import { PedidosComponent } from './modules/admin/pedidos/pedidos.component';
import { roleGuard } from './core/auth/role.guard'; 


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  
  { 
    path: 'main', 
    component: MainLayoutComponent,
    children: [
      { 
        path: 'dashboard', 
        component: DashboardComponent, 
        canActivate: [roleGuard],
        data: { role: ['ADMIN'] } 
      },
      { 
        path: 'ventas', 
        component: VentasComponent, 
        canActivate: [roleGuard], 
        data: { role: ['ADMIN', 'VENDEDOR'] } 
      },
      { 
        path: 'historial-ventas', 
        component: HistorialVentasComponent, 
        canActivate: [roleGuard], 
        data: { role: ['ADMIN', 'VENDEDOR'] } 
      },
      { 
        path: 'pedidos',
        component: PedidosComponent, 
        canActivate: [roleGuard], 
        data: { role: ['ADMIN', 'VENDEDOR', 'ALMACENERO'] } 
      },
      { 
        path: 'inventario', 
        component: InventarioComponent, 
        canActivate: [roleGuard], 
        data: { role: ['ADMIN', 'ALMACENERO'] } 
      },
          {
        path: 'clientes',
        component : ClientesComponent,
        canActivate: [roleGuard],
        data: { role: ['ADMIN'] }
      },
      
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];