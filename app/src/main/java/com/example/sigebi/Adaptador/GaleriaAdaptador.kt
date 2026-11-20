package com.example.sigebi.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataLibro
import com.example.sigebi.R

// Galería horizontal de portadas ("Novedades del catálogo" en MainActivity)
class GaleriaAdaptador(
    private var listaLibros: List<dataLibro>,
    private val alSeleccionar: ((dataLibro) -> Unit)? = null
) : RecyclerView.Adapter<GaleriaAdaptador.GaleriaViewHolder>() {

    class GaleriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPortadaLibro: ImageView = itemView.findViewById(R.id.imgPortadaGaleria)
        val txtTituloLibro: TextView = itemView.findViewById(R.id.txtTituloGaleria)
        val txtAutorLibro: TextView = itemView.findViewById(R.id.txtAutorGaleria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GaleriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro_galeria, parent, false)

        return GaleriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: GaleriaViewHolder, position: Int) {
        val libro = listaLibros[position]

        holder.txtTituloLibro.text = libro.titulo
        holder.txtAutorLibro.text = libro.autor ?: "Autor no disponible"

        // Portada con Glide desde URL_Imagen
        holder.imgPortadaLibro.cargarPortada(libro.urlImagen)

        holder.itemView.setOnClickListener { alSeleccionar?.invoke(libro) }
    }

    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun actualizarLista(nuevaLista: List<dataLibro>) {
        listaLibros = nuevaLista
        notifyDataSetChanged()
    }
}