package com.example.apk_ferreteria_yomara.data.repository

import com.example.apk_ferreteria_yomara.data.local.preferences.AuthPreferences
import com.example.apk_ferreteria_yomara.data.remote.api.FerreteriaApi
import com.example.apk_ferreteria_yomara.data.remote.dto.request.LoginRequestDto
import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
import com.example.apk_ferreteria_yomara.domain.repository.IAuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: FerreteriaApi,
    private val prefs: AuthPreferences
) : IAuthRepository {

    override suspend fun login(username: String, pass: String): Result<AuthResponseDto> {
        return try {
            val response = api.login(LoginRequestDto(username, pass))

            if (response.isSuccessful && response.body() != null) {
                val authData = response.body()!!
                prefs.saveUserData(authData)
                Result.success(authData)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}