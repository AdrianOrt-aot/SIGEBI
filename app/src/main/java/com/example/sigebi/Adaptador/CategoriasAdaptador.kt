package com.example.sigebi.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataAreas
import com.example.sigebi.R

class CategoriasAdaptador(private var listaCategorias: List<dataAreas>, private val alSeleccionar: (dataAreas) -> Unit) :
    RecyclerView.Adapter<CategoriasAdaptador.CategoriasViewHolder>() {

    class CategoriasViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtNombreCategoria: TextView = itemView.findViewById(R.id.txtNombreCategoria)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriasViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriasViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: CategoriasViewHolder,
        position: Int
    ) {
        val categoria = listaCategorias[position]
        holder.txtNombreCategoria.text = categoria.tipoLibro

        // Detectar cuando el usuario presiona una categoria
        holder.itemView.setOnClickListener {
            alSeleccionar(categoria)
        }
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }

    fun actualizarCategorias(categorias: List<dataAreas>) {
        listaCategorias = categorias
        notifyDataSetChanged()
    }
}