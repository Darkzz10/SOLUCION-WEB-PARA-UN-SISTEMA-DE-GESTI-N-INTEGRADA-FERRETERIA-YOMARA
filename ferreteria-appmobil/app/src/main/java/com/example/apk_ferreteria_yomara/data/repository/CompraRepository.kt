package com.example.apk_ferreteria_yomara.data.repository

import com.example.apk_ferreteria_yomara.data.remote.api.FerreteriaApi
import com.example.apk_ferreteria_yomara.data.remote.dto.request.CompraRequestDto
import com.example.apk_ferreteria_yomara.data.remote.dto.response.CompraResponseDto
import javax.inject.Inject

class CompraRepository @Inject constructor(
    private val api: FerreteriaApi
) {
    suspend fun registrarCompra(request: CompraRequestDto): Result<CompraResponseDto> {
        return try {
            val response = api.registrarIngreso(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error en el servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}