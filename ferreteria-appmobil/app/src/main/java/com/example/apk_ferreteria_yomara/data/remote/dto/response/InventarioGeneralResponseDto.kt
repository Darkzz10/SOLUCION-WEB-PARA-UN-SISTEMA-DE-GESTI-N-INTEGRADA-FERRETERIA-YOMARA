package com.example.apk_ferreteria_yomara.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class InventarioGeneralResponseDto(
    @SerializedName("productoId") val productoId: Long,
    @SerializedName("sku") val sku: String,
    @SerializedName("nombreProducto") val nombreProducto: String,
    @SerializedName("stockTotal") val stockTotal: Int,
    @SerializedName("detallePorSede") val detallePorSede: List<InventarioSedeDto>?
)

data class InventarioSedeDto(
    @SerializedName("sedeId") val sedeId: Long,
    @SerializedName("sedeNombre") val sedeNombre: String,
    @SerializedName("cantidadFisica") val cantidadFisica: Int,
    @SerializedName("pasillo") val pasillo: String?,
    @SerializedName("estante") val estante: String?
)