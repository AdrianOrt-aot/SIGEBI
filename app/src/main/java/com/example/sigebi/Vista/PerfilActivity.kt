package com.example.sigebi.Vista

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.sigebi.InterfazApi.ApiService
import com.example.sigebi.Modelo.RetrofitClient
import com.example.sigebi.Modelo.PerfilUsuario
import com.example.sigebi.Presentador.perfilPresentador
import com.example.sigebi.R

class PerfilActivity : AppCompatActivity(), ApiService.View {

    private lateinit var presentador: perfilPresentador
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutPerfil: LinearLayout
    private lateinit var layoutError: LinearLayout
    private lateinit var tvNombre: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var tvRol: TextView
    private lateinit var tvError: TextView
    private lateinit var btnReintentar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_perfil)

        // Inicializar componentes visuales
        progressBar = findViewById(R.id.progressBar)
        layoutPerfil = findViewById(R.id.layoutPerfil)
        layoutError = findViewById(R.id.layoutError)

        tvNombre = findViewById(R.id.tvNombre)
        tvCorreo = findViewById(R.id.tvCorreo)
        tvRol = findViewById(R.id.tvRol)
        tvError = findViewById(R.id.tvError)

        btnReintentar = findViewById(R.id.btnReintentar)

        // Obtener servicio Retrofit compartido
        val apiService = RetrofitClient.apiService
        // Inicializar presentador MVP
        presentador = perfilPresentador(
            this,
            apiService,
            lifecycleScope
        )
        // Configurar botón de reintento
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

    override fun mostrarPerfil(perfil: PerfilUsuario) {
        progressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        layoutPerfil.visibility = View.VISIBLE
        // Mostrar nombre completo
        tvNombre.text = listOfNotNull(
            perfil.Nombre,
            perfil.ApellidoP,
            perfil.ApellidoM
        ).filter {
            it.isNotBlank()
        }.joinToString(" ")
        // Mostrar correo electrónico
        tvCorreo.text =
            perfil.Correo ?: "Sin correo registrado"
        // Mostrar rol o puesto
        tvRol.text =
            perfil.Puesto ?: perfil.tipo
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