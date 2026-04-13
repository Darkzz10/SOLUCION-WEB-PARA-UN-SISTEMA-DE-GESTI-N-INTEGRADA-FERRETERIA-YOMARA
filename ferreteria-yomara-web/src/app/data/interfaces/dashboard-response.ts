export interface VentasPorSede {
  sede: string;
  monto: number;
  porcentaje: number; // Para la barra de progreso
}

export interface ProductoTop {
  nombre: string;
  cantidad: number;
  ingresoTotal: number;
  porcentaje: number; // Para la barra de progreso
}

export interface DashboardResponse {
  // Estos vienen del Backend actual
  totalVentasMes: number;
  
  // ESTOS SON NUEVOS (Simulados en el TS para este diseño)
  ventasPorSede?: VentasPorSede[];
  productosTop?: ProductoTop[];
}