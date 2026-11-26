package com.example.sigebi.Vista

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.Intent
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sigebi.InterfazApi.ApiService
import com.example.sigebi.Red.RetrofitClient
import com.example.sigebi.Modelo.PerfilUsuario
import com.example.sigebi.Presentador.clsPerfilPresentador
import com.example.sigebi.R

class PerfilActivity : AppCompatActivity(), ApiService.View {
    private lateinit var presentador: clsPerfilPresentador
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutPerfil: ScrollView
    private lateinit var layoutError: LinearLayout
    private lateinit var txtNombrePerfil: TextView
    private lateinit var txtCorreoPerfil: TextView
    private lateinit var txtTipoPerfil: TextView
    private lateinit var txtMatriculaPerfil: TextView
    private lateinit var txtTipoSesion: TextView
    private lateinit var txtCarreraPerfil: TextView
    private lateinit var tvError: TextView
    private lateinit var btnReintentar: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.perfilPrincipal)
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

        // Inicializar componentes visuales
        progressBar = findViewById(R.id.progressBar)
        layoutPerfil = findViewById(R.id.layoutPerfil)
        layoutError = findViewById(R.id.layoutError)
        txtNombrePerfil = findViewById(R.id.txtNombrePerfil)
        txtCorreoPerfil = findViewById(R.id.txtCorreoPerfil)
        txtTipoPerfil = findViewById(R.id.txtTipoPerfil)
        txtMatriculaPerfil = findViewById(R.id.txtMatriculaPerfil)
        txtTipoSesion = findViewById(R.id.txtTipoSesion)
        txtCarreraPerfil = findViewById(R.id.txtCarreraPerfil)
        tvError = findViewById(R.id.tvError)
        btnReintentar = findViewById(R.id.btnReintentar)

        // Relacionar boton cerrar sesion
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        // Obtener servicio Retrofit
        val apiService = RetrofitClient.apiService

        // Inicializar presentador
        presentador = clsPerfilPresentador(
            this,
            apiService,
            lifecycleScope
        )
        // Evento del boton reintentar
        btnReintentar.setOnClickListener {
            solicitarPerfil()
        }

        // Evento del boton cerrar sesion
        btnCerrarSesion.setOnClickListener {
            confirmarCerrarSesion()
        }

        // Configurar barra de navegacion
        configurarNavegacion()

        // Cargar perfil
        solicitarPerfil()
    }
    private fun solicitarPerfil() {
        presentador.cargarPerfil()
    }
    private fun configurarNavegacion() {

        // Ir a la pantalla de inicio
        findViewById<LinearLayout>(R.id.navInicio).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        // Ir al catalogo de libros
        findViewById<LinearLayout>(R.id.navCatalogo).setOnClickListener {
            val intent = Intent(this, ActivityCatalogo::class.java)
            startActivity(intent)
            finish()
        }

        // Ir a reservas
        findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
            startActivity(Intent(this, ActivitySolicitudes::class.java))
        }

        // Actualizar el perfil
        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener {
            solicitarPerfil()
        }
    }

    override fun mostrarCargando(mostrar: Boolean) {

        progressBar.visibility = if (mostrar) View.VISIBLE else View.GONE
        if (mostrar) {
            layoutPerfil.visibility = View.GONE
            layoutError.visibility = View.GONE
        }
    }

    override fun mostrarPerfil(perfil: PerfilUsuario) {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        layoutPerfil.visibility = View.VISIBLE

        // Mostrar nombre completo
        txtNombrePerfil.text = listOfNotNull(
            perfil.Nombre,
            perfil.ApellidoP,
            perfil.ApellidoM
        ).filter {
            it.isNotBlank()
        }.joinToString(" ")

        // Mostrar correo
        txtCorreoPerfil.text = perfil.Correo ?: "Sin correo registrado"

        // Mostrar tipo de perfil
        txtTipoPerfil.text = perfil.Puesto ?: perfil.tipo

        // Mostrar identificador del usuario
        txtMatriculaPerfil.text = perfil.identificador

        // Mostrar tipo de sesion
        txtTipoSesion.text = perfil.tipo

        // Mostrar area del usuario
        txtCarreraPerfil.text = perfil.Area ?: "No disponible"
    }

    override fun mostrarError(mensaje: String) {
        progressBar.visibility = View.GONE
        layoutPerfil.visibility = View.GONE
        layoutError.visibility = View.VISIBLE
        tvError.text = mensaje
    }

    override fun onDestroy() {
        presentador.onDestroy()
        super.onDestroy()
    }
    private fun confirmarCerrarSesion() {

        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Cerrar sesión") { _, _ ->
                cerrarSesion()
            }
            .show()
    }

    // Cerrar la sesion
    private fun cerrarSesion() {

        // Eliminar las cookies guardadas por Retrofit
        RetrofitClient.limpiarSesion()

        // Regresar al Login
        val intent = Intent(this, LoginActivity::class.java)

        // Eliminar las pantallas anteriores
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        startActivity(intent)
        finish()
    }
}