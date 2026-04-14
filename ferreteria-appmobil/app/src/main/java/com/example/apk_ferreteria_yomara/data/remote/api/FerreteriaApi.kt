package com.example.apk_ferreteria_yomara.data.remote.api

import com.example.apk_ferreteria_yomara.data.remote.dto.request.LoginRequestDto
import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProductoResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FerreteriaApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @GET("productos") //
    suspend fun buscarProductos(
        @Query("nombre") nombre: String
    ): Response<List<ProductoResponseDto>>
}