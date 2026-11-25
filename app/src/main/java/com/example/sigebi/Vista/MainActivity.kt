package com.example.sigebi.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Adaptador.GaleriaAdaptador
import com.example.sigebi.Adaptador.PrestamosAdaptador
import com.example.sigebi.Modelo.dataRespuestaResumen
import com.example.sigebi.Presentador.clsInicioPresentador
import com.example.sigebi.R
import com.example.sigebi.Red.RetrofitClient
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        // Datos que LoginActivity manda para mostrar algo mientras responde la API
        const val EXTRA_NOMBRE = "extra_nombre"
        const val EXTRA_TIPO = "extra_tipo"
    }

    private lateinit var presentador: clsInicioPresentador

    private lateinit var txtAvatar: TextView
    private lateinit var txtSaludo: TextView
    private lateinit var txtTipoUsuario: TextView
    private lateinit var txtTotalLibros: TextView
    private lateinit var txtNumPrestamos: TextView
    private lateinit var txtEtiquetaPrestamos: TextView
    private lateinit var txtNumMultas: TextView
    private lateinit var txtEtiquetaMultas: TextView
    private lateinit var txtSinPrestamos: TextView
    private lateinit var progresoInicio: ProgressBar

    private lateinit var adaptadorPrestamos: PrestamosAdaptador
    private lateinit var adaptadorNovedades: GaleriaAdaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Relacionar los componentes del XML
        txtAvatar = findViewById(R.id.txtAvatar)
        txtSaludo = findViewById(R.id.txtSaludo)
        txtTipoUsuario = findViewById(R.id.txtTipoUsuario)
        txtTotalLibros = findViewById(R.id.txtTotalLibros)
        txtNumPrestamos = findViewById(R.id.txtNumPrestamos)
        txtEtiquetaPrestamos = findViewById(R.id.txtEtiquetaPrestamos)
        txtNumMultas = findViewById(R.id.txtNumMultas)
        txtEtiquetaMultas = findViewById(R.id.txtEtiquetaMultas)
        txtSinPrestamos = findViewById(R.id.txtSinPrestamos)
        progresoInicio = findViewById(R.id.progresoInicio)

        // Mientras llega la respuesta, usar el nombre que mandó el login
        intent.getStringExtra(EXTRA_NOMBRE)?.let { mostrarNombre(it) }
        intent.getStringExtra(EXTRA_TIPO)?.let { txtTipoUsuario.text = textoTipo(it, null) }

        // Préstamos activos (lista vertical)
        adaptadorPrestamos = PrestamosAdaptador(emptyList())
        findViewById<RecyclerView>(R.id.rvPrestamosActivos).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptadorPrestamos
        }

        // Novedades del catálogo (galería horizontal con portadas)
        adaptadorNovedades = GaleriaAdaptador(emptyList()) { libro ->
            abrirCatalogo(libro.titulo)
        }
        findViewById<RecyclerView>(R.id.rvNovedades).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adaptadorNovedades
        }

        configurarNavegacion()

        presentador = clsInicioPresentador(this)
    }

    // Se recarga cada vez que se regresa a la pantalla (por ejemplo, desde el catálogo)
    override fun onResume() {
        super.onResume()
        presentador.cargarResumen()
    }

    private fun configurarNavegacion() {
        // Accesos rápidos
        findViewById<LinearLayout>(R.id.btnAccesoCatalogo).setOnClickListener { abrirCatalogo() }
        findViewById<LinearLayout>(R.id.btnAccesoReservar).setOnClickListener { abrirCatalogo() }
        findViewById<LinearLayout>(R.id.btnAccesoPrestamos).setOnClickListener {
            proximamente("Mis préstamos")
        }
        findViewById<LinearLayout>(R.id.btnAccesoPerfil).setOnClickListener {
            abrirPerfil()
        }
        findViewById<TextView>(R.id.btnVerCatalogo).setOnClickListener { abrirCatalogo() }

        // Menú inferior
        findViewById<LinearLayout>(R.id.navInicio).setOnClickListener {
            presentador.cargarResumen()
        }
        findViewById<LinearLayout>(R.id.navCatalogo).setOnClickListener { abrirCatalogo() }
        findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
            startActivity(Intent(this, ActivitySolicitudes::class.java))
        }
        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener { abrirPerfil() }
    }

    private fun abrirCatalogo(buscar: String? = null) {
        val intent = Intent(this, ActivityCatalogo::class.java)
        if (!buscar.isNullOrBlank()) {
            intent.putExtra(ActivityCatalogo.EXTRA_BUSCAR, buscar)
        }
        startActivity(intent)
    }
    //Navegación MainActivity -> PerfilActivity
    private fun abrirPerfil() {
        val intentPerfil = Intent(this, PerfilActivity::class.java)
        startActivity(intentPerfil)
    }

    private fun proximamente(pantalla: String) {
        Toast.makeText(this, "$pantalla estará disponible pronto", Toast.LENGTH_SHORT).show()
    }

    fun mostrarResumen(resumen: dataRespuestaResumen) {
        resumen.usuario?.let { usuario ->
            mostrarNombre("${usuario.nombre} ${usuario.apellidoP}")
            txtTipoUsuario.text = textoTipo(usuario.tipo, usuario.identificador)
        }

        resumen.estadisticas?.let { e ->
            txtNumPrestamos.text = e.prestamosActivos.toString()
            txtEtiquetaPrestamos.text = if (e.prestamosVencidos > 0) {
                "Préstamos activos\n(${e.prestamosVencidos} vencido${if (e.prestamosVencidos == 1) "" else "s"})"
            } else {
                "Préstamos\nactivos"
            }

            txtNumMultas.text = e.multasPendientes.toString()
            txtEtiquetaMultas.text = if (e.multasPendientes > 0) {
                String.format(Locale.US, "Multas pendientes\n$%.2f", e.montoMultas)
            } else {
                "Multas\npendientes"
            }

            txtTotalLibros.text = "${e.totalLibros} títulos en el catálogo"
        }

        adaptadorPrestamos.actualizarPrestamos(resumen.prestamos)
        txtSinPrestamos.visibility = if (resumen.prestamos.isEmpty()) View.VISIBLE else View.GONE

        adaptadorNovedades.actualizarLista(resumen.novedades)
    }

    fun mostrarCargando(cargando: Boolean) {
        progresoInicio.visibility = if (cargando) View.VISIBLE else View.GONE
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    fun sesionExpirada() {
        RetrofitClient.limpiarSesion()
        Toast.makeText(this, "Tu sesión terminó. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show()
        startActivity(
            Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    private fun mostrarNombre(nombreCompleto: String) {
        val nombre = nombreCompleto.trim()
        txtSaludo.text = "¡Hola, $nombre!"
        val iniciales = nombre.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
        txtAvatar.text = iniciales.ifEmpty { "?" }
    }

    private fun textoTipo(tipo: String, identificador: String?): String {
        val etiqueta = when (tipo.uppercase()) {
            "ALUMNO" -> "Alumno"
            "TRABAJADOR" -> "Trabajador"
            "BIBLIOTECARIO" -> "Bibliotecario"
            else -> "Usuario"
        }
        return if (identificador.isNullOrBlank()) etiqueta else "$etiqueta · $identificador"
    }

    override fun onDestroy() {
        presentador.detener()
        super.onDestroy()
    }
}
