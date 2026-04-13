export interface ProductoRequest {
  sku: string;
  nombre: string;
  descripcion: string;
  precioBase: number;
  stockMinimoGlobal: number;
  urlImagen: string;
  categoriaId: number;
}

export interface ProductoResponse {
  id: number;
  sku: string;
  nombre: string;
  descripcion: string;
  precioBase: number;
  stockMinimoGlobal: number;
  urlImagen: string;
  activo: boolean;
  categoriaId: number;
  categoriaNombre: string;
}

export type Producto = ProductoResponse;