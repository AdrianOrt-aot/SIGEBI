package com.example.sigebi.Vista

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.sigebi.InterfazApi.ApiService
import com.example.sigebi.Red.RetrofitClient
import com.example.sigebi.Modelo.dataPerfilUsuario
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_perfil)

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

        // Obtener servicio Retrofit compartido
        val apiService = RetrofitClient.apiService

        // Inicializar presentador
        presentador = clsPerfilPresentador(
            this,
            apiService,
            lifecycleScope
        )

        // Configurar boton de reintento
        btnReintentar.setOnClickListener {
            solicitarPerfil()
        }

        // Cargar perfil al iniciar
        solicitarPerfil()
    }

    private fun solicitarPerfil() {
        presentador.cargarPerfil()
    }

    override fun mostrarCargando(mostrar: Boolean) {

        progressBar.visibility =
            if (mostrar) View.VISIBLE else View.GONE

        if (mostrar) {
            layoutPerfil.visibility = View.GONE
            layoutError.visibility = View.GONE
        }
    }

    override fun mostrarPerfil(perfil: dataPerfilUsuario) {

        progressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        layoutPerfil.visibility = View.VISIBLE

        // Mostrar los datos del usuario
        txtNombrePerfil.text = perfil.nombre
        txtCorreoPerfil.text = perfil.correo

        txtTipoPerfil.text = perfil.rol ?: "Usuario"
        txtTipoSesion.text = perfil.rol ?: "Sin especificar"

        // Datos que aun no proporciona el modelo
        txtMatriculaPerfil.text = "No disponible"
        txtCarreraPerfil.text = "No disponible"
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
}