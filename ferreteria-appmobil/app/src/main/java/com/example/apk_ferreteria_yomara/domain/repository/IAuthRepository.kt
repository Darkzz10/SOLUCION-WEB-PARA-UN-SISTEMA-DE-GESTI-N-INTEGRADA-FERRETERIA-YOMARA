package com.example.apk_ferreteria_yomara.domain.repository

import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto

interface IAuthRepository {
    // el login puede fallar, por eso se usa Result
    suspend fun login(username: String, pass: String): Result<AuthResponseDto>
}