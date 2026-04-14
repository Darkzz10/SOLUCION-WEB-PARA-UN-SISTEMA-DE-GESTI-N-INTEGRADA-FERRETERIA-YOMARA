package com.example.apk_ferreteria_yomara.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ProductoResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("sku") val sku: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precioBase") val precioBase: Double,
    @SerializedName("stockMinimoGlobal") val stockMinimoGlobal: Int,
    @SerializedName("urlImagen") val urlImagen: String?,
    @SerializedName("activo") val activo: Boolean,
    @SerializedName("categoriaId") val categoriaId: Long,
    @SerializedName("categoriaNombre") val categoriaNombre: String
)