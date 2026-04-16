package com.example.apk_ferreteria_yomara.data.repository

import com.example.apk_ferreteria_yomara.data.remote.api.FerreteriaApi
import com.example.apk_ferreteria_yomara.data.remote.dto.response.VentaResponseDto
import javax.inject.Inject

class VentaRepository @Inject constructor(
    private val api: FerreteriaApi
) {
    suspend fun obtenerRutasPendientes(): Result<List<VentaResponseDto>> {
        return try {
            val response = api.obtenerRutasPendientes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}