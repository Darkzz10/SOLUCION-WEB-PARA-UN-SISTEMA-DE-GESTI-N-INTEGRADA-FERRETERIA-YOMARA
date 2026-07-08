package com.example.apk_ferreteria_yomara.ui.features.warehouse

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.databinding.FragmentHomeAlmaceneroBinding
import com.example.apk_ferreteria_yomara.ui.features.warehouse.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAlmaceneroFragment : Fragment(R.layout.fragment_home_almacenero) {

    private var _binding: FragmentHomeAlmaceneroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeAlmaceneroViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeAlmaceneroBinding.bind(view)

        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        binding.rvProductos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }

    private fun setupListeners() {
        binding.btnBuscar.setOnClickListener {
            val query = binding.etBusqueda.text.toString()
            viewModel.realizarBusqueda(query)
        }
    }

    private fun setupObservers() {
        viewModel.productos.observe(viewLifecycleOwner) { lista ->
            productAdapter.updateList(lista)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}