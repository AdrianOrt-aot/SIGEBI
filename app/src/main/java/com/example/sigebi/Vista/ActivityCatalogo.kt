package com.example.sigebi.Vista

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.Adaptador.LibrosAdaptador
import com.example.sigebi.Modelo.dataLibro
import com.example.sigebi.Presentador.clsLibrosPresentador
import com.example.sigebi.R

class ActivityCatalogo : AppCompatActivity() {
    companion object {
        // Texto a buscar al abrir el catálogo (lo manda MainActivity)
        const val EXTRA_BUSCAR = "extra_buscar"
    }

    private lateinit var presentador: clsLibrosPresentador
    private lateinit var txtBuscarLibros: EditText
    private lateinit var rvLibros: RecyclerView
    private lateinit var txtSinLibros: TextView
    private lateinit var btnRegresar: ImageView

    private lateinit var adaptador: LibrosAdaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo_libros)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Relacionar los componentes del XML
        txtBuscarLibros = findViewById(R.id.txtBuscarLibros)
        rvLibros = findViewById(R.id.rvLibros)
        txtSinLibros = findViewById(R.id.txtSinLibros)
        btnRegresar = findViewById(R.id.btnCabeceraIzquierda)

        // Configurar RecyclerView
        adaptador = LibrosAdaptador(emptyList())

        rvLibros.layoutManager = LinearLayoutManager(this)
        rvLibros.adapter = adaptador

        // Crear Presentador
        presentador = clsLibrosPresentador(this)

        // Consultar los libros al abrir el catalogo
        presentador.iniciar()

        // Buscar libros mientras escribimos
        txtBuscarLibros.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presentador.buscarLibros(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        )

        // Si MainActivity mandó un texto, buscarlo (el TextWatcher hace la búsqueda)
        intent.getStringExtra(EXTRA_BUSCAR)?.let { texto ->
            txtBuscarLibros.setText(texto)
            txtBuscarLibros.setSelection(texto.length)
        }

        // Boton regresar
        btnRegresar.setOnClickListener {
            finish()
        }

        // Menú inferior
        findViewById<LinearLayout>(R.id.navInicio).setOnClickListener { irAInicio() }
        findViewById<LinearLayout>(R.id.navCatalogo).setOnClickListener {
            // Ya estamos en el catálogo: limpiar la búsqueda y mostrar todos
            txtBuscarLibros.setText("")
        }
        findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
            Toast.makeText(this, "Reservas estará disponible pronto", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener {
            Toast.makeText(this, "Perfil estará disponible pronto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun irAInicio() {
        if (isTaskRoot) {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
        finish()
    }

    // Mostrar los libros recibidos del Presentador
    fun mostrarLibros(libros: List<dataLibro>) {
        adaptador.actualizarLibros(libros)

        if (libros.isEmpty()) {
            txtSinLibros.visibility = View.VISIBLE
        } else {
            txtSinLibros.visibility = View.GONE
        }
    }

    // Mostrar errores
    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presentador.detener()
        super.onDestroy()
    }
}