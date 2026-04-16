package com.example.apk_ferreteria_yomara.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CompraResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("numeroComprobante") val numeroComprobante: String,
    @SerializedName("total") val total: Double,
    @SerializedName("proveedorNombre") val proveedorNombre: String
)