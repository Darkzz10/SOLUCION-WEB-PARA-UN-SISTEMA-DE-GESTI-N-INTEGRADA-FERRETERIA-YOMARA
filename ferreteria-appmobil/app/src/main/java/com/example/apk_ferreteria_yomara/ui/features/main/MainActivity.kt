package com.example.apk_ferreteria_yomara.ui.features.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.data.local.preferences.AuthPreferences
import com.example.apk_ferreteria_yomara.databinding.ActivityMainBinding
import com.example.apk_ferreteria_yomara.ui.features.transport.HomeTransportistaFragment
import com.example.apk_ferreteria_yomara.ui.features.warehouse.HomeAlmaceneroFragment
import com.example.apk_ferreteria_yomara.ui.features.warehouse.IngresoProveedorFragment
import com.example.apk_ferreteria_yomara.ui.features.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
/**
 * CONTENEDOR PRINCIPAL (MAIN ACTIVITY)
 * ====================================================
 * Esta es la "Actividad Madre" de la app después del Login.
 * Su función principal es actuar como un HOST para los Fragmentos.
 *
 * CONCEPTOS IMPORTANTES
 * - @AndroidEntryPoint: Obligatorio para que Hilt pueda inyectar dependencias aquí.
 * - ViewBinding: Uso 'binding' para acceder a los IDs del XML sin hacer findViewById.
 * - Intent Extras: Recibo dinámicamente el ROL y NOMBRE para no "quemar" (hardcodear) datos.
 * - Seguridad: Implemento un Logout seguro destruyendo el Backstack.
 *
 * SU RESPONSABILIDAD ES:
 * Decidir qué Fragment se muestra inicialmente según el rol y gestionar el menú lateral
 * ===========================================================
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = intent.getStringExtra("USER_ROLE") ?: "INVITADO"
        val userName = intent.getStringExtra("USER_NAME") ?: "Usuario Yomara"

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

                menu.findItem(R.id.seccion_transporte)?.isVisible = false
                // Fragmento por defecto al iniciar sesión (El Dashboard)
                replaceFragment(HomeAlmaceneroFragment())
            }
            "TRANSPORTISTA" -> {

                menu.findItem(R.id.seccion_almacen)?.isVisible = false

                replaceFragment(HomeTransportistaFragment())
            }
            "ADMIN" -> {
                replaceFragment(HomeAlmaceneroFragment())
            }
            else -> {
                // Por seguridad, un invitado no ve nada operativo
                menu.findItem(R.id.seccion_almacen)?.isVisible = false
                menu.findItem(R.id.seccion_transporte)?.isVisible = false
            }
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_consultar_stock -> replaceFragment(HomeAlmaceneroFragment())
                R.id.nav_ingreso_proveedor -> replaceFragment(IngresoProveedorFragment())

                R.id.nav_mis_rutas -> replaceFragment(HomeTransportistaFragment())

                R.id.nav_perfil -> { /* replaceFragment(ProfileFragment()) */ }
                R.id.nav_logout -> cerrarSesionSegura()
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

    private fun cerrarSesionSegura() {

        authPreferences.clearSession()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}