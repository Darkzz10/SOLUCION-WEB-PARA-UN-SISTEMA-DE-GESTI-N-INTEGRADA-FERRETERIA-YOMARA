package com.example.apk_ferreteria_yomara.domain.repository

import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
//se pone e suspension por la llamada de la api
interface IAuthRepository {
    suspend fun login(username: String, pass: String): Result<AuthResponseDto>
}