import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../core/services/producto.service';
import { VentaService } from '../../core/services/venta.service';
import { ProductoResponse } from '../../data/interfaces/producto.interfaces';
import { ClienteService } from '../../core/services/cliente.service';

interface ItemCarrito {
  productoId: number;
  nombre: string;
  precio: number;
  cantidad: number;
  total: number;
}

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ventas.component.html',
  styleUrls: ['./ventas.component.scss']
})
export class VentasComponent implements OnInit {
  // Inyección de servicios
  private productoService = inject(ProductoService);
  private ventaService = inject(VentaService);
  private clienteService = inject(ClienteService);

  // Variables de estado
  filtro: string = '';
  productosEncontrados: ProductoResponse[] = [];
  carrito: ItemCarrito[] = [];
  totalVenta: number = 0;
  fechaHoy = new Date();
  
  // Modales
  mostrarModalVerMas: boolean = false;
  mostrarModalDespacho: boolean = false; 

  // Variables para el Modal de Cliente Nuevo
  mostrarModalNuevoCliente: boolean = false;
  nuevoClienteNombre: string = '';

  // Variables de Facturación
  tipoComprobante: string = 'BOLETA'; 
  medioPago: string = 'EFECTIVO';      
  tipoEntrega: string = 'TIENDA'; 
  dniRucCliente: string = '';
  clienteIdEncontrado: number | null = null; // ID SECRETO para el Backend
  nombreClienteEncontrado: string = '';        

  // Variables de Despacho
  direccionEntrega: string = '';       
  referenciaEntrega: string = '';
  telefonoContacto: string = '';
  fechaProgramada: string = '';

  ngOnInit(): void {}

  // --- MÉTODOS DE APOYO ---
  buscarProducto(termino?: string): void {
    const valor = termino !== undefined ? termino : this.filtro;
    if (valor.trim().length < 2) { this.productosEncontrados = []; return; }
    this.productoService.buscarParaVenta(valor).subscribe({
      next: (data) => this.productosEncontrados = data,
      error: (err) => console.error('Error al buscar:', err)
    });
  }

  agregarAlCarrito(producto: ProductoResponse): void {
    const item = this.carrito.find(i => i.productoId === producto.id);
    if (item) {
      item.cantidad++;
      item.total = item.cantidad * item.precio;
    } else {
      this.carrito.push({
        productoId: producto.id,
        nombre: producto.nombre,
        precio: producto.precioBase,
        cantidad: 1,
        total: producto.precioBase
      });
    }
    this.calcularTotalVenta();
  }

  actualizarCantidad(index: number): void {
    const item = this.carrito[index];
    if (item.cantidad <= 0) item.cantidad = 1;
    item.total = item.cantidad * item.precio;
    this.calcularTotalVenta();
  }

  eliminarDelCarrito(index: number): void {
    this.carrito.splice(index, 1);
    this.calcularTotalVenta();
  }

  calcularTotalVenta(): void {
    this.totalVenta = this.carrito.reduce((acc, item) => acc + item.total, 0);
  }

  buscarCliente() {
  console.log("🔍 Buscando DNI:", this.dniRucCliente);

  if (!this.dniRucCliente || this.dniRucCliente.trim().length < 8) {
    alert("⚠️ Ingrese un DNI o RUC válido.");
    return;
  }

  this.clienteService.buscarPorDocumento(this.dniRucCliente).subscribe({
    next: (clienteData: any) => {
      // Como Java devuelve el objeto directo, simplemente leemos sus propiedades
      if (clienteData && clienteData.id) {
        this.clienteIdEncontrado = clienteData.id;
        this.nombreClienteEncontrado = clienteData.nombreCompleto;
        console.log("Cliente validado:", this.nombreClienteEncontrado);
      } else {
        this.mostrarModalNuevoCliente = true;
      }
    },
    error: (err) => {
      console.warn("Cliente no existe en BD, abriendo modal de registro...");
      this.clienteIdEncontrado = null;
      this.nombreClienteEncontrado = '';
      
      this.mostrarModalNuevoCliente = true; 
    }
  });
}

abrirRegistroRapido() {
  alert("Cliente no registrado. Se abrirá el formulario rápido.");
  this.nuevoClienteNombre = '';
  this.mostrarModalNuevoCliente = true;
}

registrarClienteRapido() {
  if (!this.nuevoClienteNombre) {
    alert("El nombre es obligatorio"); return;
  }

  const nuevoCliente = {
    documentoNumero: this.dniRucCliente,
    nombreCompleto: this.nuevoClienteNombre,
    email: '', 
    telefono: '' 
  };

  this.clienteService.registrarCliente(nuevoCliente).subscribe({
    next: (clienteGuardado: any) => {
      this.clienteIdEncontrado = clienteGuardado.id;
      this.nombreClienteEncontrado = clienteGuardado.nombreCompleto;
      this.mostrarModalNuevoCliente = false;
      alert("✅Cliente guardado y listo para la venta");
    },
    error: (err) => alert("❌ Error al registrar cliente")
  });
}

  // --- INTERFAZ ---
  abrirModal(): void { this.mostrarModalVerMas = true; }
  cerrarModal(): void { this.mostrarModalVerMas = false; }
  seleccionarComprobante(tipo: string) { this.tipoComprobante = tipo; }
  seleccionarMedioPago(medio: string) { this.medioPago = medio; }
  
  seleccionarEntrega(tipo: string) {
  this.tipoEntrega = tipo;
  if (tipo === 'DOMICILIO') {
    this.mostrarModalDespacho = true;
  } else {
    this.limpiarDatosDespacho();
  }
}

  confirmarDatosDespacho() {
    if (this.direccionEntrega.trim().length < 5) {
      alert('⚠️ Ingresa una dirección válida.'); return;
    }
    this.mostrarModalDespacho = false;
  }

  cerrarModalDespacho() {
    if (!this.direccionEntrega) this.tipoEntrega = 'INMEDIATO';
    this.mostrarModalDespacho = false;
  }

  // ==========================================
  // LÓGICA FINAL: PROCESAR VENTA (EL CORAZÓN)
  // ==========================================
  procesarVenta(): void {
  if (this.carrito.length === 0) return;

  if (!this.clienteIdEncontrado) {
    alert('⚠️ ERROR: Debes buscar al cliente con la lupa 🔍 antes de procesar.');
    return;
  }

  const ventaRequest = {
    tipoComprobante: this.tipoComprobante,
    medioPago: this.medioPago,
    tipoEntrega: this.tipoEntrega,
    
    clienteId: this.clienteIdEncontrado, 
    
    direccionEntrega: this.direccionEntrega,
    referenciaEntrega: this.referenciaEntrega,
    telefonoContacto: this.telefonoContacto,
    fechaProgramada: this.fechaProgramada,
    descuentoTotal: 0,
    
    detalles: this.carrito.map(item => ({
      productoId: item.productoId,
      cantidad: item.cantidad,
      descuento: 0
    }))
  };

  console.log("Payload corregido que se enviará:", ventaRequest);

  this.ventaService.registrarVenta(ventaRequest).subscribe({
    next: (res) => {
      alert(`Venta exitosa: ${res.codigoOperacion}`);
      this.descargarComprobante(res.id);
      this.limpiarTerminal();
    },
    error: (err) => {
      console.error("Error detallado del servidor:", err.error);
      alert("Error: " + (err.error?.message || "Error de validación en el servidor"));
    }
  });
}

  descargarComprobante(ventaId: number): void {
    this.ventaService.descargarPdf(ventaId).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
      },
      error: (err) => console.error('Error al obtener el PDF:', err)
    });
  }

  limpiarTerminal(): void {
    this.carrito = [];
    this.totalVenta = 0;
    this.dniRucCliente = '';
    this.tipoEntrega = 'TIENDA';
    this.tipoComprobante = 'BOLETA';
    this.medioPago = 'EFECTIVO';
    this.limpiarDatosDespacho();
    this.filtro = '';
    this.productosEncontrados = [];
  }

  private limpiarDatosDespacho(): void {
    this.direccionEntrega = '';
    this.referenciaEntrega = '';
    this.telefonoContacto = '';
    this.fechaProgramada = '';
  }
}