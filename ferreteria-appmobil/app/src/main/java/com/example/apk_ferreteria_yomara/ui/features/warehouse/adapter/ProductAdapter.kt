package com.example.apk_ferreteria_yomara.ui.features.warehouse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.apk_ferreteria_yomara.R
import com.example.apk_ferreteria_yomara.data.remote.dto.response.ProductoResponseDto
import com.example.apk_ferreteria_yomara.databinding.ItemProductBinding

class ProductAdapter(
    private var productos: List<ProductoResponseDto> = emptyList()
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: ProductoResponseDto) {
            binding.tvProductName.text = producto.nombre
            binding.tvCategory.text = producto.categoriaNombre
            binding.tvSku.text = "SKU: ${producto.sku}"

            binding.tvPrice.text = "Stock Mín.: ${producto.stockMinimoGlobal} und."

            val imageTarget = producto.urlImagen ?: ""

            val urlImagenMasHeaders = GlideUrl(
                imageTarget,
                LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .addHeader("Referer", "https://www.google.com/") // Engañamos al servidor externo
                    .build()
            )

            Glide.with(binding.root.context)
                .load(if (imageTarget.isNotEmpty()) urlImagenMasHeaders else R.drawable.ferret)
                .placeholder(R.drawable.ferret)
                .error(R.drawable.consultar)
                .centerCrop()
                .into(binding.ivProductImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productos[position])
    }

    override fun getItemCount(): Int = productos.size

    fun updateList(nuevaLista: List<ProductoResponseDto>) {
        this.productos = nuevaLista
        notifyDataSetChanged()
    }
}