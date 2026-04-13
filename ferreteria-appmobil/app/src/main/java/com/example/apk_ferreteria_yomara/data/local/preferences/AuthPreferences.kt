package com.example.apk_ferreteria_yomara.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.apk_ferreteria_yomara.data.remote.dto.response.AuthResponseDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context //le indica a Hilt que use el Context de la App
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "auth_prefs_yomara",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getAuthToken(): String? {
        return sharedPrefs.getString("jwt_token", null)
    }

    fun clearSession() {
        sharedPrefs.edit().clear().apply()
    }


    fun saveUserData(response: AuthResponseDto) {
        sharedPrefs.edit().apply {
            putString("jwt_token", response.token)
            putString("user_role", response.rol)
            putString("user_name", response.nombreCompleto)
            putLong("sede_id", response.sedeId)
            apply()
        }
    }

    fun getUserRole(): String = sharedPrefs.getString("user_role", "GUEST") ?: "GUEST"
    fun getUserName(): String = sharedPrefs.getString("user_name", "") ?: ""
}