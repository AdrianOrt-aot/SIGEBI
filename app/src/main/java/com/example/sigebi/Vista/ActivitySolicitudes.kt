package com.example.sigebi.Vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sigebi.R
import com.example.sigebi.Adaptador.SolicitudesAdaptador
import com.example.sigebi.Red.RetrofitClient
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class ActivitySolicitudes : AppCompatActivity() {
    private val adaptador = SolicitudesAdaptador()
    private lateinit var txtVacio: TextView
    private lateinit var progreso: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitudes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtVacio = findViewById(R.id.txtSolicitudesVacias)
        progreso = findViewById(R.id.progresoSolicitudes)

        val rvSolicitudes = findViewById<RecyclerView>(R.id.rvSolicitudes)

        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter = adaptador

        findViewById<ImageView>(R.id.btnVolverSolicitudes).setOnClickListener { finish() }

        findViewById<View>(R.id.btnActualizarSolicitudes).setOnClickListener { cargarSolicitudes() }
    }

    override fun onResume() {
        super.onResume()
        cargarSolicitudes()
    }

    private fun cargarSolicitudes() {
        progreso.visibility = View.VISIBLE
        txtVacio.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.obtenerSolicitudes()

                if (respuesta.code() == 401) {
                    RetrofitClient.limpiarSesion()

                    startActivity(Intent(this@ActivitySolicitudes, LoginActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )

                    finish()
                    return@launch
                }

                if (!respuesta.isSuccessful || respuesta.body()?.ok != true) {
                    throw IllegalStateException(RetrofitClient.mensajeDeError(respuesta) ?:
                    "No se pudieron consultar las solicitudes")
                }

                val solicitudes = respuesta.body()?.data.orEmpty()

                //Mostrar solo las solicitudes pendientes
                val pendientes = solicitudes.filter {
                    it.estado == "PENDIENTE"
                }

                adaptador.actualizar(pendientes)

                txtVacio.text = "No tienes solicitudes pendientes"
                txtVacio.visibility =
                    if (pendientes.isEmpty())
                        View.VISIBLE
                    else
                        View.GONE
            } catch (e: Exception) {
                adaptador.actualizar(emptyList())

                txtVacio.text = "No fue posible cargar las solicitudes"
                txtVacio.visibility = View.VISIBLE

                Toast.makeText(this@ActivitySolicitudes, e.message ?: "Error de conexión", Toast.LENGTH_LONG
                ).show()
            } finally {
                progreso.visibility = View.GONE
            }
        }
    }
}