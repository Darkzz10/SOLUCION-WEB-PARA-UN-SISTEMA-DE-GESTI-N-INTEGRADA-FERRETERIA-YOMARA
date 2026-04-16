package com.example.apk_ferreteria_yomara.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class VentaResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("codigoOperacion") val codigoOperacion: String,
    @SerializedName("estadoVenta") val estadoVenta: String,
    @SerializedName("tipoEntrega") val tipoEntrega: String,
    @SerializedName("direccionEntrega") val direccionEntrega: String?,
    @SerializedName("clienteNombre") val clienteNombre: String,
    @SerializedName("clienteDocumento") val clienteDocumento: String,
    @SerializedName("total") val total: Double
)