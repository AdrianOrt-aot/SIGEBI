
package com.example.sigebi.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataLibro

class LibrosAdaptador(
    private var listaLibros: List<dataLibro>
) : RecyclerView.Adapter<LibrosAdaptador.LibrosViewHolder>() {

    class LibrosViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtTitulo: TextView =
            itemView.findViewById(android.R.id.text1)

        val txtAutor: TextView =
            itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibrosViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(
                android.R.layout.simple_list_item_2,
                parent,
                false
            )

        return LibrosViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: LibrosViewHolder,
        position: Int
    ) {

        val libro = listaLibros[position]

        holder.txtTitulo.text = libro.titulo
        holder.txtAutor.text =
            libro.autor ?: "Autor no disponible"
    }

    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun actualizarLibros(libros: List<dataLibro>) {
        listaLibros = libros
        notifyDataSetChanged()
    }
}
