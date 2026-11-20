package com.example.sigebi.Adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Modelo.dataPrestamoActivo
import com.example.sigebi.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Préstamos activos del usuario en MainActivity (usa item_prestamo.xml)
class PrestamosAdaptador(
    private var listaPrestamos: List<dataPrestamoActivo>
) : RecyclerView.Adapter<PrestamosAdaptador.PrestamoViewHolder>() {

    class PrestamoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPortada: ImageView = itemView.findViewById(R.id.imgPortadaPrestamo)
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTituloPrestamo)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstadoPrestamo)
        val txtAutor: TextView = itemView.findViewById(R.id.txtAutorPrestamo)
        val txtFechaPrestamo: TextView = itemView.findViewById(R.id.txtFechaPrestamo)
        val txtFechaDevolucion: TextView = itemView.findViewById(R.id.txtFechaDevolucion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestamoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestamo, parent, false)
        return PrestamoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: PrestamoViewHolder, position: Int) {
        val prestamo = listaPrestamos[position]

        holder.txtTitulo.text = prestamo.titulo
        holder.txtAutor.text = prestamo.autor ?: "Autor no disponible"
        holder.txtFechaPrestamo.text = "Fecha de préstamo: ${formatearFecha(prestamo.fechaPrestamo)}"
        holder.txtFechaDevolucion.text = "Devolución: ${formatearFecha(prestamo.fechaDevolucion)}"
        holder.imgPortada.cargarPortada(prestamo.urlImagen)

        if (prestamo.vencido) {
            holder.txtEstado.text = "Vencido"
            holder.txtEstado.setBackgroundResource(R.drawable.bg_fondo_badge_rojo)
            holder.txtEstado.setTextColor(Color.parseColor("#B3261E"))
        } else {
            holder.txtEstado.text = "En curso"
            holder.txtEstado.setBackgroundResource(R.drawable.bg_fondo_badge)
            holder.txtEstado.setTextColor(Color.parseColor("#075B0C"))
        }
    }

    override fun getItemCount(): Int = listaPrestamos.size

    fun actualizarPrestamos(prestamos: List<dataPrestamoActivo>) {
        listaPrestamos = prestamos
        notifyDataSetChanged()
    }

    // "2026-07-20" -> "20/07/2026"
    private fun formatearFecha(fecha: String?): String {
        if (fecha.isNullOrBlank()) return "--/--/----"
        return try {
            LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            fecha
        }
    }
}
