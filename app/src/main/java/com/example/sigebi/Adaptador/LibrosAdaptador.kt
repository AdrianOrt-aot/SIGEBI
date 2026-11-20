package com.example.sigebi.Adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataLibro
import com.example.sigebi.R

// Adaptador del catálogo: usa la tarjeta item_libro_catalogo.xml con la portada de la BD
class LibrosAdaptador(
    private var listaLibros: List<dataLibro>,
    private val alSeleccionar: ((dataLibro) -> Unit)? = null
) : RecyclerView.Adapter<LibrosAdaptador.LibrosViewHolder>() {

    class LibrosViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgPortada: ImageView = itemView.findViewById(R.id.imgPortadaLibro)
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTituloLibro)
        val txtAutor: TextView = itemView.findViewById(R.id.txtAutorLibro)
        val txtDisponibilidad: TextView = itemView.findViewById(R.id.txtDisponibilidadLibro)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibrosViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro_catalogo, parent, false)

        return LibrosViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: LibrosViewHolder,
        position: Int
    ) {

        val libro = listaLibros[position]

        holder.txtTitulo.text = libro.titulo
        holder.txtAutor.text = libro.autor ?: "Autor no disponible"

        // Portada desde la base de datos (tbllibro.URL_Imagen)
        holder.imgPortada.cargarPortada(libro.urlImagen)

        // Disponibilidad según los ejemplares
        val disponibles = libro.disponibles ?: 0
        if (disponibles > 0) {
            holder.txtDisponibilidad.text =
                if (disponibles == 1) "1 disponible" else "$disponibles disponibles"
            holder.txtDisponibilidad.setBackgroundResource(R.drawable.bg_fondo_badge_rojo)
            holder.txtDisponibilidad.setTextColor(Color.parseColor("#075B0C"))
        } else {
            holder.txtDisponibilidad.text = "No disponible"
            holder.txtDisponibilidad.setBackgroundResource(R.drawable.bg_fondo_badge_rojo)
            holder.txtDisponibilidad.setTextColor(Color.parseColor("#B3261E"))
        }

        holder.itemView.setOnClickListener { alSeleccionar?.invoke(libro) }
    }

    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun actualizarLibros(libros: List<dataLibro>) {
        listaLibros = libros
        notifyDataSetChanged()
    }
}