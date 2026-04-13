package com.example.apk_ferreteria_yomara

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class RecepcionProveedorActivity : AppCompatActivity() {

    private lateinit var tietProducto: TextInputEditText
    private lateinit var tietCantidad: TextInputEditText
    private lateinit var btnAgregar: Button
    private lateinit var lvMercaderia: ListView
    private lateinit var btnConfirmar: Button

    private var listaMercaderia = mutableListOf<String>()
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recepcion_proveedor)

        tietProducto = findViewById(R.id.tietProducto)
        tietCantidad = findViewById(R.id.tietCantidad)
        btnAgregar = findViewById(R.id.btnAgregar)
        lvMercaderia = findViewById(R.id.lvMercaderia)
        btnConfirmar = findViewById(R.id.btnConfirmar)

        listAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaMercaderia
        )
        lvMercaderia.adapter = listAdapter


        btnAgregar.setOnClickListener {
            val producto = tietProducto.text?.trim().toString()
            val cantidad = tietCantidad.text?.trim().toString()

            if (producto.isNotEmpty() && cantidad.isNotEmpty()) {
                val itemRegistro = "[$cantidad unid.] - $producto"
                listaMercaderia.add(itemRegistro)
                listAdapter.notifyDataSetChanged()

                tietProducto.text?.clear()
                tietCantidad.text?.clear()
                tietProducto.requestFocus()
            } else {
                Toast.makeText(this, "Debe ingresar el producto y la cantidad", Toast.LENGTH_SHORT).show()
            }
        }

        lvMercaderia.setOnItemClickListener { _, _, position, _ ->
            val item = listaMercaderia[position]
            Toast.makeText(this, "Seleccionado: $item", Toast.LENGTH_SHORT).show()
        }

        lvMercaderia.setOnItemLongClickListener { _, _, position, _ ->
            val item = listaMercaderia[position]
            listaMercaderia.removeAt(position)
            listAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Registro eliminado: $item", Toast.LENGTH_SHORT).show()
            true
        }

        btnConfirmar.setOnClickListener {
            if (listaMercaderia.isNotEmpty()) {
                // Aquí iría la lógica real para consumir la API y guardar en BD
                Toast.makeText(this, "Sincronizando ${listaMercaderia.size} ítems con la base de datos...", Toast.LENGTH_LONG).show()

                // limpiamos lista
                listaMercaderia.clear()
                listAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "La lista de recepción está vacía", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}