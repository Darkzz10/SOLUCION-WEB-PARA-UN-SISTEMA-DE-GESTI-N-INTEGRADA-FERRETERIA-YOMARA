package com.example.apk_ferreteria_yomara.data.repository

import com.example.apk_ferreteria_yomara.data.remote.api.FerreteriaApi
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProductoResponseDto
import com.example.apk_ferreteria_yomara.domain.repository.IProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: FerreteriaApi
) : IProductRepository {
    override suspend fun buscarProductos(query: String): Result<List<ProductoResponseDto>> {
        return try {
            val response = api.buscarProductos(query)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al traer productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}