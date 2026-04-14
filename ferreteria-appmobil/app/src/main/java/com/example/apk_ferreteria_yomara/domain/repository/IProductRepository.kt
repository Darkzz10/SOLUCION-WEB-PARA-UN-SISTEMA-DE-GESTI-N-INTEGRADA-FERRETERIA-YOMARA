package com.example.apk_ferreteria_yomara.domain.repository

import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProductoResponseDto

interface IProductRepository {
    suspend fun buscarProductos(query: String): Result<List<ProductoResponseDto>>
}