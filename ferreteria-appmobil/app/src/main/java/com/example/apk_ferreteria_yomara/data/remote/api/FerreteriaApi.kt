package com.example.apk_ferreteria_yomara.data.remote.api

import com.example.apk_ferreteria_yomara.data.remote.dto.request.LoginRequestDto
import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FerreteriaApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>
}