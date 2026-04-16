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
/**
 *GESTOR DE INVENTARIO (HOME ALMACENERO VIEWMODEL)
 * ====================================================
 * Este ViewModel es el motor de búsqueda de la Ferretería Yomara.
 * Su misión es procesar las consultas del almacenero y transformar la data
 * que llega de Spring Boot en información útil para la lista (RecyclerView).
 *
 * CONCEPTOS
 * viewModelScope.launch:  ejecuta la búsqueda en SEGUNDO PLANO. Esto
 * permite que el almacenero vea el "Cargando" sin que la app se trabe.
 * GetProductsUseCase: Aplicamos Clean Architecture. No llamamos a la API
 * directo, pasamos por un Caso de Uso para mantener el código ordenado.
 * Filtro de Seguridad (.filter): Aunque el servidor debería filtrar, aquí
 * aplicamos un "doble check" en el cliente para asegurar que solo se pinten
 * productos que coincidan con el SKU o Nombre ingresado.
 * Encapsulamiento de Datos: Usamos _productos (Mutable) para cambiar la
 * lista y productos (LiveData) para que la UI solo la lea.
 *
 * SU RESPONSABILIDAD ES:
 * Controlar el flujo de búsqueda de productos, gestionar el estado de carga
 * (ProgressBar) y entregar la lista filtrada de mercadería a la vista.
 * ===========================================================
 */
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