package com.example.sigebi.Vista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sigebi.Adaptador.cargarPortada
import com.example.sigebi.Modelo.dataPeticionSolicitud
import com.example.sigebi.Modelo.dataDetalleLibro
import com.example.sigebi.Presentador.clsDetalleLibroPresentador
import com.example.sigebi.R
import com.example.sigebi.Red.RetrofitClient
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class ActivityDetalleLibro : AppCompatActivity() {
    private var libroActual: dataDetalleLibro? = null
    private var enviandoSolicitud = false
    private var solicitudRegistrada = false
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

        // Ajustar la pantalla a las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.detallePrincipal)
        ) { vista, insets ->

            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            vista.setPadding(
                barras.left,
                barras.top,
                barras.right,
                barras.bottom
            )
            insets
        }

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
        btnReservarLibro.setOnClickListener { confirmarSolicitud() }

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
        libroActual = libro
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
        btnReservarLibro.isEnabled = libro.disponibles > 0 && !enviandoSolicitud && !solicitudRegistrada
    }
    private fun confirmarSolicitud() {
        val libro = libroActual ?: return

        if (enviandoSolicitud || solicitudRegistrada)
            return
        AlertDialog.Builder(this).setTitle("Solicitar préstamo").setMessage(
            "¿Deseas solicitar «${libro.titulo}» " + "para recogerlo en la biblioteca?"
        ).setNegativeButton("Cancelar", null).setPositiveButton("Solicitar"){ _, _ ->
            registrarSolicitud(libro.idLibro)
        }.show()
    }
    private fun registrarSolicitud(idLibro: Int){
        if(enviandoSolicitud)
            return
        enviandoSolicitud = true
        btnReservarLibro.isEnabled = false
        btnReservarLibro.text = "Solicitando..."
        lifecycleScope.launch {
            try {
                //Paso 1: Obtener token CSRF
                val respuestaToken = RetrofitClient.apiService.obtenerTokenCsrf()

                if (respuestaToken.code() == 401){
                    sesionExpirada()
                    return@launch
                }

                if (!respuestaToken.isSuccessful || respuestaToken.body()?.ok != true){
                    throw IllegalStateException(RetrofitClient.mensajeDeError(respuestaToken) ?:
                    "No se pudo obtener el token de seguridad")
                }

                val token = respuestaToken.body()?.token ?: throw IllegalStateException("El servidor no proporcionó el token CSRF")

                //Paso 2: solicitar el libro
                val respuesta = RetrofitClient.apiService.crearSolicitud(token, dataPeticionSolicitud(idLibro))

                if (respuesta.code() == 401){
                    sesionExpirada()
                    return@launch
                }

                if (!respuesta.isSuccessful || respuesta.body()?.ok != true){
                    throw IllegalStateException(RetrofitClient.mensajeDeError(respuesta) ?:
                    respuesta.body()?.mensaje ?:
                    "No se pudo registrar la solicitud")
                }

                //Paso 3: mostrar confirmacion
                solicitudRegistrada = true
                Toast.makeText(this@ActivityDetalleLibro, respuesta.body()?.mensaje ?:
                "Solicitud pendiente de entrega", Toast.LENGTH_LONG).show()

                //Paso 4: mostrar las solicitudes
                abrirSolicitudes()
            } catch (e: Exception) {
                Toast.makeText(this@ActivityDetalleLibro, e.message ?: "Error de conexión", Toast.LENGTH_LONG).show()
            } finally {
                enviandoSolicitud = false

                if (solicitudRegistrada) {
                    btnReservarLibro.text = "Solicitud realizada"
                    btnReservarLibro.isEnabled = false
                } else {
                    btnReservarLibro.text = "Solicitar préstamo"
                    btnReservarLibro.isEnabled = libroActual?.disponibles?.let { it > 0 } ?: false
                }
            }
        }
    }
    private fun abrirSolicitudes() {
        startActivity(Intent(this, ActivitySolicitudes::class.java))
    }
    private fun sesionExpirada() {
        RetrofitClient.limpiarSesion()
        Toast.makeText(this, "Tu sesión expiró. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
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