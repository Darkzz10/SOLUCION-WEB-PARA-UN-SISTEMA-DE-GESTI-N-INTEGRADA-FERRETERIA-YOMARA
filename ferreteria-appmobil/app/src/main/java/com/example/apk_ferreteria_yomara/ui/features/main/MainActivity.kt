package com.example.apk_ferreteria_yomara.ui.features.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.databinding.ActivityMainBinding
import com.example.apk_ferreteria_yomara.ui.features.delivery.HomeTransportistaFragment
import com.example.apk_ferreteria_yomara.ui.features.warehouse.HomeAlmaceneroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("USER_ROLE") ?: "ALMACENERO"
        val userName = "Darek"

        setupToolbar()
        setupDrawer(userRole, userName)
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupDrawer(role: String, name: String) {
        val navView = binding.navigationView
        val menu = navView.menu

        val headerView = navView.getHeaderView(0)
        val tvName = headerView.findViewById<TextView>(R.id.tvUserName)
        val tvRole = headerView.findViewById<TextView>(R.id.tvUserRole)
        tvName.text = "Hola, $name"
        tvRole.text = "Rol: $role"

        when (role) {
            "ALMACENERO" -> {
                menu.findItem(R.id.nav_transporte)?.isVisible = false
                replaceFragment(HomeAlmaceneroFragment())
            }
            "TRANSPORTISTA" -> {
                menu.findItem(R.id.nav_almacen)?.isVisible = false
                replaceFragment(HomeTransportistaFragment())
            }
            "ADMIN" -> {
                replaceFragment(HomeAlmaceneroFragment())
            }
        }

        // 3. Listener de los clics
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_almacen -> replaceFragment(HomeAlmaceneroFragment())
                R.id.nav_transporte -> replaceFragment(HomeTransportistaFragment())
                R.id.nav_perfil -> { /* replaceFragment(ProfileFragment()) */ }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}