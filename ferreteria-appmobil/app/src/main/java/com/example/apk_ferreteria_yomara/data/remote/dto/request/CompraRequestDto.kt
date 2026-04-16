package com.example.apk_ferreteria_yomara.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CompraRequestDto(
    @SerializedName("numeroComprobante") val numeroComprobante: String, // Cambiado para matchear tu backend
    @SerializedName("proveedorId") val proveedorId: Long,
    @SerializedName("detalles") val detalles: List<CompraDetalleRequestDto>
)

data class CompraDetalleRequestDto(
    @SerializedName("productoId") val productoId: Long,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precioCompra") val precioCompra: Double
)