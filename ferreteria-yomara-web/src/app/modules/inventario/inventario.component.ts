import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../core/services/producto.service';
import { ProductoResponse, ProductoRequest } from '../../data/interfaces/producto.interfaces';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventario.component.html'
})
export class InventarioComponent implements OnInit {
  private productoService = inject(ProductoService);

  // Estas son las variables que el HTML lee
  productos: ProductoResponse[] = [];
  productosFiltrados: ProductoResponse[] = [];
  
  terminoBusqueda: string = '';
  isLoading = true;

  // Variables para el Modal
  showModal = false;
  isEditing = false;
  productoIdSelected?: number;
  formProducto: ProductoRequest = this.resetForm();

  ngOnInit(): void {
    this.cargarProductos();
  }

  resetForm(): ProductoRequest {
    return {
      sku: '', nombre: '', descripcion: '',
      precioBase: 0, stockMinimoGlobal: 0,
      urlImagen: '', categoriaId: 1
    };
  }

  cargarProductos(): void {
    this.isLoading = true;
    
    // 💡 OJO: Usamos listarActivos() igual que en la red para asegurar que llegue el Taladro
    this.productoService.listarActivos().subscribe({
      next: (data) => {
        console.log('1. Datos que llegan del API:', data);
        this.productos = data;
        this.productosFiltrados = data; // ¡AQUÍ SE LLENA LA TABLA!
        console.log('2. Datos asignados a la variable HTML:', this.productosFiltrados);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar inventario:', err);
        this.isLoading = false;
      }
    });
  }

  filtrarProductos(): void {
    if (!this.terminoBusqueda) {
      this.productosFiltrados = this.productos;
      return;
    }
    const termino = this.terminoBusqueda.toLowerCase();
    this.productosFiltrados = this.productos.filter(p => 
      p.nombre.toLowerCase().includes(termino) || 
      p.sku.toLowerCase().includes(termino)
    );
  }

  // --- LÓGICA DEL CRUD ---

  abrirModalNuevo() {
    this.isEditing = false;
    this.formProducto = this.resetForm();
    this.showModal = true;
  }

  abrirModalEditar(prod: ProductoResponse) {
    this.isEditing = true;
    this.productoIdSelected = prod.id;
    this.formProducto = {
      sku: prod.sku,
      nombre: prod.nombre,
      descripcion: prod.descripcion,
      precioBase: prod.precioBase,
      stockMinimoGlobal: prod.stockMinimoGlobal,
      urlImagen: prod.urlImagen,
      categoriaId: prod.categoriaId
    };
    this.showModal = true;
  }

  guardar() {
    if (this.isEditing && this.productoIdSelected) {
      this.productoService.actualizar(this.productoIdSelected, this.formProducto).subscribe({
        next: () => { this.showModal = false; this.cargarProductos(); },
        error: (err) => alert('Error al actualizar')
      });
    } else {
      this.productoService.crear(this.formProducto).subscribe({
        next: () => { this.showModal = false; this.cargarProductos(); },
        error: (err) => alert('Error al crear')
      });
    }
  }

  desactivar(id: number, nombre: string): void {
    if(confirm(`¿Desactivar ${nombre}?`)) {
      this.productoService.desactivar(id).subscribe(() => this.cargarProductos());
    }
  }
}