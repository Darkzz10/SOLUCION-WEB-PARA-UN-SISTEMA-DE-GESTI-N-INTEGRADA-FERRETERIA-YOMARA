package com.example.apk_ferreteria_yomara.ui.features.warehouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_ferreteria_yomara.data.remote.dto.request.CompraDetalleRequestDto
import com.example.apk_ferreteria_yomara.data.remote.dto.request.CompraRequestDto
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProveedorResponseDto
import com.example.apk_ferreteria_yomara.domain.usecase.RegistrarCompraUseCase
import com.example.apk_ferreteria_yomara.domain.usecase.GetProveedoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngresoProveedorViewModel @Inject constructor(
    private val registrarCompraUseCase: RegistrarCompraUseCase,
    private val getProveedoresUseCase: GetProveedoresUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> = _mensajeError

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    private val _productosSeleccionados = MutableLiveData<MutableList<CompraDetalleRequestDto>>(mutableListOf())
    val productosSeleccionados: LiveData<MutableList<CompraDetalleRequestDto>> = _productosSeleccionados

    // Guardamos la lista de proveedores reales
    private val _proveedores = MutableLiveData<List<ProveedorResponseDto>>()
    val proveedores: LiveData<List<ProveedorResponseDto>> = _proveedores

    init {
        cargarProveedores()
    }

    private fun cargarProveedores() {
        viewModelScope.launch {
            val result = getProveedoresUseCase()

            result.onSuccess { lista ->
                _proveedores.value = lista
            }.onFailure { exception ->
                _mensajeError.value = "Error al cargar proveedores: ${exception.message}"
            }
        }
    }

    fun agregarProductoALista(productoId: Long, cantidad: Int, precio: Double) {
        val listaActual = _productosSeleccionados.value ?: mutableListOf()
        listaActual.add(CompraDetalleRequestDto(productoId, cantidad, precio))
        _productosSeleccionados.value = listaActual
    }

    fun validarFormulario(proveedorId: Long?, comprobante: String): Boolean {
        if (proveedorId == null) {
            _mensajeError.value = "Debe seleccionar un proveedor"
            return false
        }
        if (comprobante.isEmpty()) {
            _mensajeError.value = "Debe ingresar el número de comprobante"
            return false
        }
        if (_productosSeleccionados.value.isNullOrEmpty()) {
            _mensajeError.value = "Debe agregar al menos un producto"
            return false
        }
        _mensajeError.value = null
        return true
    }

    fun registrarIngreso(proveedorId: Long, comprobante: String) {
        if (!validarFormulario(proveedorId, comprobante)) return

        _isLoading.value = true

        val request = CompraRequestDto(
            numeroComprobante = comprobante,
            proveedorId = proveedorId,
            detalles = _productosSeleccionados.value ?: emptyList()
        )

        viewModelScope.launch {
            val result = registrarCompraUseCase(request)

            result.onSuccess {
                _registroExitoso.value = true
                _productosSeleccionados.value = mutableListOf()
            }.onFailure { exception ->
                _mensajeError.value = "Error al registrar: ${exception.message}"
            }

            _isLoading.value = false
        }
    }
}