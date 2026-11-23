package com.example.sigebi.Vista

import android.os.Bundle
import android.widget.Button
import com.example.sigebi.R
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sigebi.Adaptador.cargarPortada
import com.example.sigebi.Modelo.dataDetalleLibro
import com.example.sigebi.Presentador.clsDetalleLibroPresentador

class ActivityDetalleLibro : AppCompatActivity() {
    private lateinit var presentador: clsDetalleLibroPresentador
    private lateinit var imgPortadaDetalle: ImageView
    private lateinit var txtTituloDetalle: TextView
    private lateinit var txtAutorDetalle: TextView
    private lateinit var txtEstadoDetalle: TextView
    private lateinit var txtISBN: TextView
    private lateinit var txtCategoriaDetalle: TextView
    private lateinit var txtEditorial: TextView
    private lateinit var txtEjemplares: TextView
    private lateinit var txtDescripcionLibro: TextView
    private lateinit var btnRegresar: ImageView
    private lateinit var btnReservarLibro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_libro)

        // Relacionar los componentes del XML
        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        txtTituloDetalle = findViewById(R.id.txtTituloDetalle)
        txtAutorDetalle = findViewById(R.id.txtAutorDetalle)
        txtEstadoDetalle = findViewById(R.id.txtEstadoDetalle)
        txtISBN = findViewById(R.id.txtISBN)
        txtCategoriaDetalle = findViewById(R.id.txtCategoriaDetalle)
        txtEditorial = findViewById(R.id.txtEditorial)
        txtEjemplares = findViewById(R.id.txtEjemplares)
        txtDescripcionLibro = findViewById(R.id.txtDescripcionLibro)
        btnRegresar = findViewById(R.id.btnCabeceraIzquierda)
        btnReservarLibro = findViewById(R.id.btnReservarLibro)

        // No mostrar datos de ejemplo del XML
        txtDescripcionLibro.text = "Descripción no disponible"

        // Las reservas pertenecen a otro módulo
        btnReservarLibro.isEnabled = false

        // Crear el presentador
        presentador = clsDetalleLibroPresentador(this)

        // Recibir el ID enviado desde el catálogo
        val idLibro = intent.getIntExtra("idLibro", 0)
        if (idLibro > 0) {
            presentador.cargarDetalle(idLibro)
        } else {
            mostrarError("No se recibió un ID de libro válido")
        }
        // Regresar a la pantalla anterior
        btnRegresar.setOnClickListener {
            finish()
        }
    }
    // Mostrar los datos que entrega el Presentador
    fun mostrarDetalle(libro: dataDetalleLibro) {
        txtTituloDetalle.text = libro.titulo
        txtAutorDetalle.text = libro.autor ?: "Autor no disponible"
        txtISBN.text = libro.isbn
        txtCategoriaDetalle.text = libro.tipoLibro
        txtEditorial.text = libro.editorial
        txtEjemplares.text = libro.disponibles.toString()

        if (libro.disponibles > 0) {
            txtEstadoDetalle.text = "Disponible"
        } else {
            txtEstadoDetalle.text = "No disponible"
        }
        // Cargar la portada con Glide
        imgPortadaDetalle.cargarPortada(libro.urlImagen)
    }

    // Mostrar mensaje cuando ocurra un error
    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
    override fun onDestroy() {
        presentador.detener()
        super.onDestroy()
    }
}