package com.example.apk_ferreteria_yomara.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)