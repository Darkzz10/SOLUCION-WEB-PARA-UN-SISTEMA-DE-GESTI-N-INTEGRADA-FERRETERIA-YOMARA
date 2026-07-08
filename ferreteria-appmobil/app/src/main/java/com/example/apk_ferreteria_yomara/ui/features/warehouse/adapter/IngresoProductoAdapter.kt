package com.example.apk_ferreteria_yomara.ui.features.warehouse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apk_ferreteria_yomara.data.remote.dto.request.CompraDetalleRequestDto
import com.example.apk_ferreteria_yomara.databinding.ItemProductoIngresoBinding

class IngresoProductoAdapter(
    private var detalles: List<CompraDetalleRequestDto> = emptyList()
) : RecyclerView.Adapter<IngresoProductoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemProductoIngresoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductoIngresoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = detalles[position]

        holder.binding.tvProductoNombre.text = "Producto ID: ${item.productoId}"
        holder.binding.tvCantidadIngreso.text = "Cant: ${item.cantidad}"
        holder.binding.tvSubtotalIngreso.text = "S/ ${item.precioCompra}"
    }

    override fun getItemCount() = detalles.size

    fun updateList(nuevaLista: List<CompraDetalleRequestDto>) {
        this.detalles = nuevaLista
        notifyDataSetChanged()
    }
}