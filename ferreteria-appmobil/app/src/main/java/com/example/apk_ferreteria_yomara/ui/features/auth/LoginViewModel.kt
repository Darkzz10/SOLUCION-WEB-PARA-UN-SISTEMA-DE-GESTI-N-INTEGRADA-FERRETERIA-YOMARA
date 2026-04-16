package com.example.apk_ferreteria_yomara.ui.features.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_ferreteria_yomara.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * APUNTE: GESTOR DE ESTADOS Y DATOS (LOGIN VIEWMODEL)
 * ====================================================
 * Este es el "Intermediario" entre la vista (Fragment) y la lógica (UseCase).
 * Su trabajo es sobrevivir a cambios de configuración (como rotar el celular)
 * y mantener los datos vivos mientras se consulta al servidor.
 *
 * CONCEPTOS CLAVE:
 * Encapsulamiento (_mutable vs inmutable): se usa variables privadas para
 * modificar datos y variables públicas para que la Vista solo las lea.
 * LiveData: Es el canal de comunicación. Cuando el valor cambia, la Vista
 * se entera automáticamente y se "pinta" sola.
 * viewModelScope.launch:  AQUÍ NACE EL SEGUNDO PLANO. Es la corrutina que
 * evita que la app se congele. El login viaja por internet en este bloque sin
 * trabar el "Hilo Principal" (el que el usuario toca).
 * Manejo de Estados: Controla el 'isLoading' para mostrar el ProgressBar y
 * el 'errorMessage' para avisar si algo salió mal.
 *
 * SU RESPONSABILIDAD ES:
 * Preparar los datos para la interfaz, disparar el proceso de autenticación
 * en segundo plano y exponer los resultados (éxito/error) de forma segura.
 * ===========================================================
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    // variable para saber si  esta cargando
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // variable para error
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // variable para avisar que el login fue exitoso y pasar el ROL
    private val _userRole = MutableLiveData<String?>()
    val userRole: LiveData<String?> get() = _userRole

    fun login(username: String, pass: String) {
        if (username.isEmpty() || pass.isEmpty()) {
            _errorMessage.value = "Por favor, completa todos los campos"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        // se ejecuta la llamada al backend en una corrutina
        viewModelScope.launch {
            val result = loginUseCase(username, pass)

            // se quita el "cargando" apenas termine la llamada
            _isLoading.value = false

            if (result.isSuccess) {
                // se obtiene el rol del objeto que devolvió el backend
                val authData = result.getOrNull()
                _userRole.value = authData?.rol
            } else {
                _errorMessage.value = "Usuario o clave incorrectos"
            }
        }
    }
}