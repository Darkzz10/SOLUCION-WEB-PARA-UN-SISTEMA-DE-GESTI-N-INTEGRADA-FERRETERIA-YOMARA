package com.example.apk_ferreteria_yomara.ui.features.warehouse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProductoResponseDto
import com.example.apk_ferreteria_yomara.databinding.ItemProductBinding

class ProductAdapter(
    private var productos: List<ProductoResponseDto> = emptyList()
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // 1. El "Molde" (ViewHolder): Aquí enlazamos las vistas del XML con el código
    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: ProductoResponseDto) {
            binding.tvProductName.text = producto.nombre
            binding.tvCategory.text = producto.categoriaNombre
            binding.tvPrice.text = "S/ ${String.format("%.2f", producto.precioBase)}"
            binding.tvSku.text = producto.sku

            // Si el producto estuviera inactivo, podríamos cambiar el color aquí,
            // pero como filtramos solo activos en el backend, lo dejamos normal.
        }
    }

    // 2. Inflamos el XML de item_product por cada fila
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    // 3. Le pasamos los datos a la fila correspondiente
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productos[position])
    }

    // 4. ¿Cuántos productos hay?
    override fun getItemCount(): Int = productos.size

    // 5. Función vital para actualizar la lista cuando el Almacenero busca algo
    fun updateList(nuevaLista: List<ProductoResponseDto>) {
        this.productos = nuevaLista
        notifyDataSetChanged() // ⚠️ Aviso visual de que la data cambió
    }
}