package com.example.apk_ferreteria_yomara.domain.usecase

import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
import com.example.apk_ferreteria_yomara.domain.repository.IAuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    // El operador 'invoke' permite llamar a la clase como si fuera una función: loginUseCase(user, pass)
    suspend operator fun invoke(username: String, pass: String): Result<AuthResponseDto> {
        return repository.login(username, pass)
    }
}