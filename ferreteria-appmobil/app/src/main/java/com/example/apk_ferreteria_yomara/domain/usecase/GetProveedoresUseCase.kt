package com.example.apk_ferreteria_yomara.domain.usecase

import com.example.apk_ferreteria_yomara.data.repository.ProveedorRepository
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProveedorResponseDto
import javax.inject.Inject

class GetProveedoresUseCase @Inject constructor(
    private val repository: ProveedorRepository
) {
    // Usamos el operador 'invoke' para poder llamar a la clase como si fuera una función
    suspend operator fun invoke(): Result<List<ProveedorResponseDto>> {
        return repository.obtenerProveedores()
    }
}