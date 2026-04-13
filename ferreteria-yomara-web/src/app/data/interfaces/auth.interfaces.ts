export interface LoginRequest {
  username: string;
  password: string;
}


export interface AuthResponse {
  token: string;
  username: string;
  rol: string;
  sedeId: number;
  sedeNombre: string;
  nombreCompleto: string;
}


export interface UsuarioResponse {
  id: number;
  username: string;
  nombreCompleto: string;
  rol: string;
  activo: boolean;
  sedeId: number;
  sedeNombre: string;
}