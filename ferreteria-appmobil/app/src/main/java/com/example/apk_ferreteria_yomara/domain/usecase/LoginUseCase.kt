package com.example.apk_ferreteria_yomara.domain.usecase

import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
import com.example.apk_ferreteria_yomara.domain.repository.IAuthRepository
import javax.inject.Inject

/**
 * CASO DE USO (LOGIN)
 * ====================================================
 * este Use Case sirve para que el ViewModel no hable
 * directamente con el repositorio, manteniendo todo desacoplado. "Clean Architecture"
 *
 * CONCEPTOS IMPORTANTES
 * @Inject: se pide a Hilt que  traiga el Repositorio necesario.
 * operator fun invoke: permite usar la clase como si fuera una función para ahorrar código en el ViewModel
 * suspend: Indica que esto corre dentro de una Corrutina. No bloquea la UI
 * mientras esperamos la respuesta del servidor de la ferretería.
 * Result<T>: Un contenedor profesional que nos dice si la operación
 * fue exitosa o si hubo un error (como clave incorrecta o falta de internet).
 *
 * SU RESPONSABILIDAD ES:
 * ejecutar la validación de credenciales consultando al repositorio y devolver el token de acceso o el error correspondiente.
 * ===========================================================
 */

class LoginUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    // El operador "invoke" permite llamar a la clase como si fuera una función: loginUseCase(user, pass)
    suspend operator fun invoke(username: String, pass: String): Result<AuthResponseDto> {
        return repository.login(username, pass)
    }
}