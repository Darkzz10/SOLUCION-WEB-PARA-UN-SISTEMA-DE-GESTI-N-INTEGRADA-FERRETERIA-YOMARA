package com.example.apk_ferreteria_yomara.domain.usecase

import com.example.apk_ferreteria_yomara.data.repository.CompraRepository
import com.example.apk_ferreteria_yomara.data.remote.dto.request.CompraRequestDto
import javax.inject.Inject

class RegistrarCompraUseCase @Inject constructor(
    private val repository: CompraRepository
) {
    suspend operator fun invoke(request: CompraRequestDto) = repository.registrarCompra(request)
}