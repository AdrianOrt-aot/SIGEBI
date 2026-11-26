package com.example.sigebi.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Adaptador.PrestamosAdaptador
import com.example.sigebi.R
import com.example.sigebi.Adaptador.SolicitudesAdaptador
import com.example.sigebi.Red.RetrofitClient
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import kotlin.coroutines.cancellation.CancellationException

class ActivitySolicitudes : AppCompatActivity() {
    private val adaptadorPrestamos = PrestamosAdaptador(emptyList())
    private val adaptadorSolicitudes = SolicitudesAdaptador()
    private lateinit var rvPrestamosReservas: RecyclerView
    private lateinit var txtVacio: TextView
    private lateinit var txtTituloListado: TextView
    private lateinit var btnVerPrestamos: LinearLayout
    private lateinit var btnVerReservas: LinearLayout
    private var pestanaActual = "PRESTAMOS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_prestamos_reservas)

        // Ajustar la pantalla a las barras de Android
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.prestamosPrincipal)
        ) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Relacionar los componentes del XML
        rvPrestamosReservas = findViewById(R.id.rvPrestamosReservas)
        txtVacio = findViewById(R.id.txtSinPrestamos)
        txtTituloListado = findViewById(R.id.txtTituloListado)

        btnVerPrestamos = findViewById(R.id.btnVerPrestamos)
        btnVerReservas = findViewById(R.id.btnVerReservas)

        // Configurar RecyclerView
        rvPrestamosReservas.layoutManager = LinearLayoutManager(this)
        rvPrestamosReservas.adapter = adaptadorPrestamos

        // Evento del boton Prestamos
        btnVerPrestamos.setOnClickListener {
            mostrarPrestamos()
        }

        // Evento del boton Reservas
        btnVerReservas.setOnClickListener {
            mostrarReservas()
        }

        // Configurar navegacion inferior
        configurarNavegacion()
    }

    override fun onResume() {
        super.onResume()

        if (pestanaActual == "PRESTAMOS") {
            mostrarPrestamos()
        } else {
            mostrarReservas()
        }
    }

    // Mostrar la pestaña de prestamos
    private fun mostrarPrestamos() {

        pestanaActual = "PRESTAMOS"

        txtTituloListado.text = "Préstamos activos"
        txtVacio.visibility = View.GONE
        btnVerPrestamos.setBackgroundResource(R.drawable.bg_dashboard_nav_active)
        btnVerReservas.setBackgroundResource(R.drawable.bg_fondo_tarjeta)
        rvPrestamosReservas.adapter = adaptadorPrestamos

        cargarPrestamos()
    }

    // Mostrar la pestaña de reservas
    private fun mostrarReservas() {

        pestanaActual = "RESERVAS"
        txtTituloListado.text = "Solicitudes pendientes"
        txtVacio.visibility = View.GONE
        btnVerPrestamos.setBackgroundResource(R.drawable.bg_fondo_tarjeta)
        btnVerReservas.setBackgroundResource(R.drawable.bg_dashboard_nav_active)
        rvPrestamosReservas.adapter = adaptadorSolicitudes

        cargarSolicitudes()
    }

    // Consultar prestamos activos del usuario
    private fun cargarPrestamos() {

        lifecycleScope.launch {

            try {
                val respuesta = RetrofitClient.apiService.obtenerResumen()

                // Comprobar la sesion
                if (respuesta.code() == 401) {
                    sesionExpirada()
                    return@launch
                }

                if (!respuesta.isSuccessful ||
                    respuesta.body()?.ok != true) {

                    throw IllegalStateException(RetrofitClient.mensajeDeError(respuesta) ?: "No se pudieron consultar los préstamos")
                }

                // Obtener prestamos desde la API
                val prestamos = respuesta.body()?.prestamos.orEmpty()

                adaptadorPrestamos.actualizarPrestamos(prestamos)

                if (pestanaActual == "PRESTAMOS") {

                    txtVacio.text = "No tienes préstamos activos"
                    txtVacio.visibility =
                        if (prestamos.isEmpty())
                            View.VISIBLE
                        else
                            View.GONE
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                if (pestanaActual == "PRESTAMOS") {

                    adaptadorPrestamos.actualizarPrestamos(emptyList())
                    txtVacio.text = "No fue posible cargar los préstamos"
                    txtVacio.visibility = View.VISIBLE

                    Toast.makeText(
                        this@ActivitySolicitudes,
                        e.message ?: "Error de conexión",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Consultar solicitudes pendientes
    private fun cargarSolicitudes() {

        lifecycleScope.launch {

            try {
                val respuesta = RetrofitClient.apiService.obtenerSolicitudes()

                // Comprobar la sesion
                if (respuesta.code() == 401) {
                    sesionExpirada()
                    return@launch
                }

                if (!respuesta.isSuccessful || respuesta.body()?.ok != true) {

                    throw IllegalStateException(RetrofitClient.mensajeDeError(respuesta) ?: "No se pudieron consultar las solicitudes")
                }

                // Obtener solicitudes desde la API
                val solicitudes = respuesta.body()?.data.orEmpty()

                // Mostrar solamente las pendientes
                val pendientes = solicitudes.filter {
                    it.estado.equals(
                        "PENDIENTE",
                        ignoreCase = true
                    )
                }

                adaptadorSolicitudes.actualizar(pendientes)

                if (pestanaActual == "RESERVAS") {

                    txtVacio.text = "No tienes solicitudes pendientes"

                    txtVacio.visibility =
                        if (pendientes.isEmpty())
                            View.VISIBLE
                        else
                            View.GONE
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                if (pestanaActual == "RESERVAS") {

                    adaptadorSolicitudes.actualizar(emptyList())

                    txtVacio.text = "No fue posible cargar las solicitudes"

                    txtVacio.visibility = View.VISIBLE

                    Toast.makeText(
                        this@ActivitySolicitudes,
                        e.message ?: "Error de conexión",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Configurar la barra de navegacion
    private fun configurarNavegacion() {

        // Ir a Inicio
        findViewById<LinearLayout>(R.id.navInicio)
            .setOnClickListener {

                val intent = Intent(
                    this,
                    MainActivity::class.java
                )

                startActivity(intent)
                finish()
            }

        // Ir al Catalogo
        findViewById<LinearLayout>(R.id.navCatalogo)
            .setOnClickListener {

                val intent = Intent(
                    this,
                    ActivityCatalogo::class.java
                )

                startActivity(intent)
                finish()
            }

        // Permanecer en Reservas
        findViewById<LinearLayout>(R.id.navReservas)
            .setOnClickListener {

                mostrarReservas()
            }

        // Ir al Perfil
        findViewById<LinearLayout>(R.id.navPerfil)
            .setOnClickListener {

                val intent = Intent(
                    this,
                    PerfilActivity::class.java
                )

                startActivity(intent)
                finish()
            }
    }

    // Cuando la sesion ha expirado
    private fun sesionExpirada() {

        RetrofitClient.limpiarSesion()

        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        startActivity(intent)
        finish()
    }
}