package com.example.sigebi.Vista

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sigebi.Presentador.clsRecuperacionPresentador
import com.example.sigebi.R

class RecuperarPasswordActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IDENTIFICADOR = "extra_identificador"
    }

    private lateinit var presentador: clsRecuperacionPresentador

    private lateinit var rgTipoCuenta: RadioGroup
    private lateinit var txtIdentificador: EditText
    private lateinit var btnEnviarEnlace: Button
    private lateinit var txtMensaje: TextView
    private lateinit var progreso: ProgressBar
    private lateinit var txtYaTengoCodigo: TextView
    private lateinit var layoutRestablecer: LinearLayout
    private lateinit var txtCodigo: EditText
    private lateinit var txtNuevaContrasena: EditText
    private lateinit var txtConfirmarContrasena: EditText
    private lateinit var btnRestablecer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        presentador = clsRecuperacionPresentador(this)

        rgTipoCuenta = findViewById(R.id.rgTipoCuenta)
        txtIdentificador = findViewById(R.id.txtIdentificadorRecuperar)
        btnEnviarEnlace = findViewById(R.id.btnEnviarEnlace)
        txtMensaje = findViewById(R.id.txtMensajeRecuperar)
        progreso = findViewById(R.id.progresoRecuperar)
        txtYaTengoCodigo = findViewById(R.id.txtYaTengoCodigo)
        layoutRestablecer = findViewById(R.id.layoutRestablecer)
        txtCodigo = findViewById(R.id.txtCodigo)
        txtNuevaContrasena = findViewById(R.id.txtNuevaContrasena)
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena)
        btnRestablecer = findViewById(R.id.btnRestablecer)

        // Identificador escrito en el login
        intent.getStringExtra(EXTRA_IDENTIFICADOR)?.let { txtIdentificador.setText(it) }

        btnEnviarEnlace.setOnClickListener {
            presentador.solicitarRecuperacion(tipoSeleccionado(), txtIdentificador.text.toString())
        }

        txtYaTengoCodigo.setOnClickListener { mostrarPasoCodigo(layoutRestablecer.visibility != View.VISIBLE) }

        btnRestablecer.setOnClickListener {
            presentador.restablecerPassword(
                tipoSeleccionado(),
                txtIdentificador.text.toString(),
                txtCodigo.text.toString(),
                txtNuevaContrasena.text.toString(),
                txtConfirmarContrasena.text.toString()
            )
        }

        findViewById<TextView>(R.id.txtVolverLogin).setOnClickListener { finish() }
    }

    // ALUMNO = matrícula (tblalumnos), TRABAJADOR = número de control (tbltrabajador)
    private fun tipoSeleccionado(): String =
        if (rgTipoCuenta.checkedRadioButtonId == R.id.rbTrabajador) "TRABAJADOR" else "ALUMNO"

    private fun mostrarPasoCodigo(mostrar: Boolean) {
        layoutRestablecer.visibility = if (mostrar) View.VISIBLE else View.GONE
        txtYaTengoCodigo.text = if (mostrar) "Ocultar código" else "Ya tengo un código"
    }

    // ----------------- Métodos que llama el Presentador -----------------

    fun mostrarCargando(cargando: Boolean) {
        progreso.visibility = if (cargando) View.VISIBLE else View.GONE
        btnEnviarEnlace.isEnabled = !cargando
        btnRestablecer.isEnabled = !cargando
    }

    fun solicitudEnviada(mensaje: String) {
        mostrarMensaje(mensaje + "\nRevisa tu correo: puedes abrir el enlace o pegar aquí el código.", false)
        mostrarPasoCodigo(true)
    }

    fun passwordRestablecida(mensaje: String) {
        Toast.makeText(this, "$mensaje. Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show()
        finish()
    }

    fun mostrarError(mensaje: String) {
        mostrarMensaje(mensaje, true)
    }

    private fun mostrarMensaje(mensaje: String, esError: Boolean) {
        txtMensaje.text = mensaje
        txtMensaje.setTextColor(Color.parseColor(if (esError) "#B3261E" else "#1E7A37"))
        txtMensaje.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        presentador.detener()
        super.onDestroy()
    }
}
