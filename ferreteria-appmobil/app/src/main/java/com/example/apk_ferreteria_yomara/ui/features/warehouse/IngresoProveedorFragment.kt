package com.example.apk_ferreteria_yomara.ui.features.warehouse

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProveedorResponseDto
import com.example.apk_ferreteria_yomara.databinding.FragmentIngresoProveedorBinding
import com.example.apk_ferreteria_yomara.ui.features.warehouse.adapter.IngresoProductoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngresoProveedorFragment : Fragment(R.layout.fragment_ingreso_proveedor) {

    private var _binding: FragmentIngresoProveedorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IngresoProveedorViewModel by viewModels()
    private lateinit var ingresoAdapter: IngresoProductoAdapter

    private var listaProveedoresReales: List<ProveedorResponseDto> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIngresoProveedorBinding.bind(view)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        ingresoAdapter = IngresoProductoAdapter()
        binding.rvProductosIngreso.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvProductosIngreso.adapter = ingresoAdapter

        // Botón para agregar producto a la lista
        binding.btnAgregarProducto.setOnClickListener {
            viewModel.agregarProductoALista(productoId = 1L, cantidad = 5, precio = 245.0)
            Toast.makeText(requireContext(), "Producto agregado a la lista", Toast.LENGTH_SHORT).show()
        }

        // Botón para Guardar el Ingreso en la Base de Datos
        binding.btnGuardarIngreso.setOnClickListener {
            val textoSeleccionado = binding.actvProveedor.text.toString()
            val guia = binding.etNumeroGuia.text.toString()

            val proveedorElegido = listaProveedoresReales.find {
                it.razonSocial == textoSeleccionado
            }

            if (proveedorElegido != null) {
                viewModel.registrarIngreso(proveedorId = proveedorElegido.id, comprobante = guia)
            } else {
                Toast.makeText(requireContext(), "Por favor, seleccione un proveedor válido de la lista", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {

        viewModel.proveedores.observe(viewLifecycleOwner) { proveedores ->
            listaProveedoresReales = proveedores

            val nombresProveedores = proveedores.map { it.razonSocial }

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                nombresProveedores
            )
            binding.actvProveedor.setAdapter(adapter)
        }

        viewModel.productosSeleccionados.observe(viewLifecycleOwner) { lista ->
            ingresoAdapter.updateList(lista)
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Ingreso registrado en BD", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack() // Volvemos a la pantalla anterior
            }
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), " $it", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnGuardarIngreso.isEnabled = !isLoading
            binding.btnGuardarIngreso.text = if (isLoading) "REGISTRANDO..." else "REGISTRAR INGRESO AL SISTEMA"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}