package com.example.apk_ferreteria_yomara.ui.features.transport


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.databinding.FragmentHomeTransportistaBinding
import com.example.apk_ferreteria_yomara.ui.features.transport.adapter.RutasAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeTransportistaFragment : Fragment(R.layout.fragment_home_transportista) {

    private var _binding: FragmentHomeTransportistaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeTransportistaViewModel by viewModels()
    private lateinit var rutasAdapter: RutasAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeTransportistaBinding.bind(view)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        rutasAdapter = RutasAdapter()
        binding.rvRutas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rutasAdapter
        }
    }

    private fun setupObservers() {
        viewModel.rutas.observe(viewLifecycleOwner) { lista ->
            rutasAdapter.updateList(lista)

            binding.tvContadorRutas.text = "Tienes ${lista.size} entregas pendientes"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { cargando ->
            if (cargando) {
                binding.tvContadorRutas.text = "Buscando rutas..."
            }
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}