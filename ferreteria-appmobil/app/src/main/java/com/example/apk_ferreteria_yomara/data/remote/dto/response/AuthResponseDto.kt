package com.example.apk_ferreteria_yomara.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("username") val username: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("sedeId") val sedeId: Long,
    @SerializedName("sedeNombre") val sedeNombre: String,
    @SerializedName("nombreCompleto") val nombreCompleto: String
)