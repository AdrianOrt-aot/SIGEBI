package com.example.sigebi.Adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataSolicitudPrestamo
import com.example.sigebi.R

class SolicitudesAdaptador : RecyclerView.Adapter<SolicitudesAdaptador.Holder>() {
    private var solicitudes = listOf<dataSolicitudPrestamo>()

    class Holder(vista: View) : RecyclerView.ViewHolder(vista) {
        val portada: ImageView = vista.findViewById(R.id.imgPortadaPrestamo)
        val titulo: TextView = vista.findViewById(R.id.txtTituloPrestamo)
        val estado: TextView = vista.findViewById(R.id.txtEstadoPrestamo)
        val autor: TextView = vista.findViewById(R.id.txtAutorPrestamo)
        val fecha: TextView = vista.findViewById(R.id.txtFechaPrestamo)
        val devolucion: TextView = vista.findViewById(R.id.txtFechaDevolucion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):Holder {
        val vista = LayoutInflater.from(parent.context).inflate(
            R.layout.item_prestamo, parent, false)

        return Holder(vista)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val solicitud = solicitudes[position]

        holder.titulo.text = solicitud.titulo
        holder.estado.text = "Pendiente de entrega"
        holder.estado.setTextColor(Color.parseColor("#8A5800"))
        holder.estado.setBackgroundColor(Color.parseColor("#FFF2CC"))
        holder.autor.text = "Solicitud #${solicitud.idSolicitud}"
        holder.fecha.text = "Solicitado: ${solicitud.fechaSolicitud.take(10)}"
        holder.devolucion.text = "Acude a la biblioteca para recogerlo"
        holder.portada.cargarPortada(solicitud.urlImagen)
    }

    override fun getItemCount(): Int {
        return solicitudes.size
    }

    fun actualizar(nuevas: List<dataSolicitudPrestamo>) {
        solicitudes = nuevas
        notifyDataSetChanged()
    }
}