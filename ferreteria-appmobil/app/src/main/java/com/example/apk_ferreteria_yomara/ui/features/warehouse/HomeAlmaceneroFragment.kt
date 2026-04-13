package com.example.apk_ferreteria_yomara.ui.features.warehouse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apk_ferreteria_yomara.data.local.preferences.AuthPreferences
import com.example.apk_ferreteria_yomara.databinding.FragmentHomeAlmaceneroBinding
import com.example.apk_ferreteria_yomara.ui.features.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeAlmaceneroFragment : Fragment() {

    private var _binding: FragmentHomeAlmaceneroBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authPreferences: AuthPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAlmaceneroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCerrarSesion.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        authPreferences.clearSession()

        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}