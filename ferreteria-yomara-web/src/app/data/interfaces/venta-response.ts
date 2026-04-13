export interface VentaResponse {
  id: number;
  codigoOperacion: string;
  tipoComprobante: string;
  medioPago: string;
  total: number;

  estado: string;
  estadoVenta:string | null | undefined;
  fechaVenta: string;
  
  // Logística
  tipoEntrega: string; 
  direccionEntrega?: string;
  referenciaEntrega?: string;
  telefonoContacto?: string;
  fechaProgramada?: string;
  
  // Cliente
  clienteId: number;
  clienteDocumento: string;
  clienteNombre: string;
}