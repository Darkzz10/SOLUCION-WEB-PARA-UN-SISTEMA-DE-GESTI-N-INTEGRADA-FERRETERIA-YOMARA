package com.example.apk_ferreteria_yomara.ui.features.warehouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProductoResponseDto
import com.example.apk_ferreteria_yomara.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeAlmaceneroViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productos = MutableLiveData<List<ProductoResponseDto>>()
    val productos: LiveData<List<ProductoResponseDto>> = _productos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun realizarBusqueda(query: String) {
        if (query.isEmpty()) return

        _isLoading.value = true
        viewModelScope.launch {
            val result = getProductsUseCase(query)
            _isLoading.value = false

            if (result.isSuccess) {
                val listaCompleta = result.getOrNull() ?: emptyList()

                val listaFiltrada = listaCompleta.filter { producto ->
                    producto.nombre.lowercase().contains(query.lowercase()) ||
                            producto.sku.lowercase().contains(query.lowercase())
                }

                _productos.value = listaFiltrada
            } else {
            }
        }
    }
}