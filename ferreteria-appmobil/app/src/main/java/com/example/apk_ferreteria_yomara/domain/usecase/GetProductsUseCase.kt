package com.example.apk_ferreteria_yomara.domain.usecase

import com.example.apk_ferreteria_yomara.domain.repository.IProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(query: String) = repository.buscarProductos(query)
}