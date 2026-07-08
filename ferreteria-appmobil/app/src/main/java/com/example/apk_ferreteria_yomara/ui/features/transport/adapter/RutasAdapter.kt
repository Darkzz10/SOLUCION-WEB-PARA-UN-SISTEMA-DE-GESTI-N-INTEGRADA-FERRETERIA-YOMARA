package com.example.apk_ferreteria_yomara.ui.features.transport.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apk_ferreteria_yomara.data.remote.dto.response.VentaResponseDto
import com.example.apk_ferreteria_yomara.databinding.ItemRutaEntregaBinding

class RutasAdapter(
    private var rutas: List<VentaResponseDto> = emptyList()
) : RecyclerView.Adapter<RutasAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRutaEntregaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRutaEntregaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ruta = rutas[position]

        holder.binding.tvClienteNombre.text = ruta.clienteNombre
        holder.binding.tvCodigoVenta.text = ruta.codigoOperacion
        holder.binding.tvDireccion.text = ruta.direccionEntrega ?: "Dirección no especificada"

        holder.binding.btnVerRuta.setOnClickListener {
        }
    }

    override fun getItemCount() = rutas.size

    fun updateList(nuevaLista: List<VentaResponseDto>) {
        this.rutas = nuevaLista
        notifyDataSetChanged()
    }
}