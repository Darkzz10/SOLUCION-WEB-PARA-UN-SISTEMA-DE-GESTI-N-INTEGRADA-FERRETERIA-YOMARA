import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteService, ClienteResponse, ClienteRequest } from '../../core/services/cliente.service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {
  private clienteService = inject(ClienteService);

  clientes: ClienteResponse[] = [];
  filtroDocumento: string = '';
  
  mostrarModal = false;
  modoEdicion = false;
  clienteIdActual: number | null = null;

  // Formulario
  clienteForm: ClienteRequest = {
    documentoNumero: '',
    nombreCompleto: '',
    email: '',
    telefono: ''
  };

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes() {
    this.clienteService.listarClientes().subscribe({
      next: (res) => {
        // Extraemos la lista del objeto paginado "content"
        this.clientes = res.content || [];
      },
      error: (err) => console.error('Error cargando clientes', err)
    });
  }

  buscarCliente() {
    if (!this.filtroDocumento) {
      this.cargarClientes();
      return;
    }
    this.clienteService.buscarPorDocumento(this.filtroDocumento).subscribe({
      next: (res) => this.clientes = res.content || [],
      error: () => this.clientes = []
    });
  }

  abrirModalNuevo() {
    this.modoEdicion = false;
    this.clienteForm = { documentoNumero: '', nombreCompleto: '', email: '', telefono: '' };
    this.mostrarModal = true;
  }

  abrirModalEditar(cliente: ClienteResponse) {
    this.modoEdicion = true;
    this.clienteIdActual = cliente.id;
    this.clienteForm = { 
      documentoNumero: cliente.documentoNumero, 
      nombreCompleto: cliente.nombreCompleto, 
      email: cliente.email, 
      telefono: cliente.telefono 
    };
    this.mostrarModal = true;
  }

  guardarCliente() {
    if (this.modoEdicion && this.clienteIdActual) {
      this.clienteService.actualizarCliente(this.clienteIdActual, this.clienteForm).subscribe({
        next: () => {
          alert('✅ Cliente actualizado');
          this.mostrarModal = false;
          this.cargarClientes();
        }
      });
    } else {
      this.clienteService.registrarCliente(this.clienteForm).subscribe({
        next: () => {
          alert('✅ Cliente registrado');
          this.mostrarModal = false;
          this.cargarClientes();
        }
      });
    }
  }
}