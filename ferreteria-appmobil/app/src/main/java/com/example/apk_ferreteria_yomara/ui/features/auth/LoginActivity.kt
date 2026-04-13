package com.example.apk_ferreteria_yomara.ui.features.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.data.local.preferences.AuthPreferences
import com.example.apk_ferreteria_yomara.ui.features.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authPreferences.getAuthToken() != null) {
            val role = authPreferences.getUserRole()
            navigateToMain(role)
            return
        }

        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginContainer, LoginFragment())
                .commit()
        }
    }

    private fun navigateToMain(role: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_ROLE", role)
        }
        startActivity(intent)
        finish()
    }
}