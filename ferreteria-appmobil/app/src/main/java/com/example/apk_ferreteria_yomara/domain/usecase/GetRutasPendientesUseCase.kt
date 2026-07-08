package com.example.apk_ferreteria_yomara.domain.usecase

import com.example.apk_ferreteria_yomara.data.repository.VentaRepository
import com.example.apk_ferreteria_yomara.data.remote.dto.response.VentaResponseDto
import javax.inject.Inject

class GetRutasPendientesUseCase @Inject constructor(
    private val repository: VentaRepository
) {
    suspend operator fun invoke(): Result<List<VentaResponseDto>> {
        return repository.obtenerRutasPendientes()
    }
}