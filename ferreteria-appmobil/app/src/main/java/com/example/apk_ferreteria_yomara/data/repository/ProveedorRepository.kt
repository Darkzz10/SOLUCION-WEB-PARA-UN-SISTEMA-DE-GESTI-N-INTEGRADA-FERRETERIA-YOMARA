package com.example.apk_ferreteria_yomara.data.repository

import com.example.apk_ferreteria_yomara.data.remote.api.FerreteriaApi
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProveedorResponseDto
import javax.inject.Inject

class ProveedorRepository @Inject constructor(
    private val api: FerreteriaApi
) {
    suspend fun obtenerProveedores(): Result<List<ProveedorResponseDto>> {
        return try {
            val response = api.obtenerProveedores()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error en el servidor: código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}