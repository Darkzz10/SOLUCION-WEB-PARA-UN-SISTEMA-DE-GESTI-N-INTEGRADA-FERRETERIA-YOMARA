package com.example.apk_ferreteria_yomara.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ProveedorResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("razonSocial") val razonSocial: String, // 🚀 CORREGIDO AQUÍ
    @SerializedName("ruc") val ruc: String
)