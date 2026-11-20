package com.example.sigebi.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataLibro
import com.example.sigebi.R

class GaleriaAdaptador(private var listaLibros: List<dataLibro>):
    RecyclerView.Adapter<GaleriaAdaptador.GaleriaViewHolder>() {
    // Antes tenemos que crear una clase Holder
    class GaleriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPortadaLibro: ImageView = itemView.findViewById(R.id.imgPortadaLibro)
        val txtTituloLibro: TextView = itemView.findViewById(R.id.txtTituloLibro)
        val txtAutorLibro: TextView = itemView.findViewById(R.id.txtAutorLibro)
        val txtDisponibilidadLibro: TextView = itemView.findViewById(R.id.txtDisponibilidadLibro)
    }

    // Estas 3 funciones se van a programar. Son contratos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GaleriaViewHolder {
        // Vamos a inflar el layout item_libro_catalogo.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro_catalogo, parent, false)

        return GaleriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: GaleriaViewHolder, position: Int) {
        val libro = listaLibros[position]

        // Aqui colocaremos los datos del libro
        // utilizando las propiedades de dataLibro

        // Aqui cargaremos la portada con Glide
        // cuando tengamos la propiedad de la imagen
    }

    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun actualizarLista(nuevaLista: List<dataLibro>) {
        listaLibros = nuevaLista
        notifyDataSetChanged()
    }
}