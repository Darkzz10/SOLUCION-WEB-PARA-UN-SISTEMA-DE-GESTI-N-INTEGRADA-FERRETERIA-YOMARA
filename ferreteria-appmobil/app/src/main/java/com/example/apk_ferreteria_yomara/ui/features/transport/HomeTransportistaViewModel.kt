package com.example.apk_ferreteria_yomara.ui.features.transport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_ferreteria_yomara.data.remote.dto.response.VentaResponseDto
import com.example.apk_ferreteria_yomara.domain.usecase.GetRutasPendientesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeTransportistaViewModel @Inject constructor(
    private val getRutasPendientesUseCase: GetRutasPendientesUseCase
) : ViewModel() {

    private val _rutas = MutableLiveData<List<VentaResponseDto>>()
    val rutas: LiveData<List<VentaResponseDto>> = _rutas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> = _mensajeError

    init {
        cargarRutas()
    }

    private fun cargarRutas() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getRutasPendientesUseCase()

            result.onSuccess { listaCompleta ->

                val despachoHoy = listaCompleta.filter { venta ->
                    venta.tipoEntrega == "DOMICILIO" &&
                            venta.estadoVenta == "PENDIENTE_DESPACHO"
                }

                _rutas.value = despachoHoy

            }.onFailure { exception ->
                _mensajeError.value = "Error de conexión: ${exception.message}"
            }
            _isLoading.value = false
        }
    }
}