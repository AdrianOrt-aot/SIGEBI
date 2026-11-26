
package com.example.sigebi.Vista

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sigebi.R
import com.example.sigebi.Modelo.UsuarioLogin
import com.example.sigebi.Presentador.clsLoginPresentador
import android.content.Intent
import com.example.sigebi.Vista.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var presentador: clsLoginPresentador
    private lateinit var txtUsuario: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var btnIngresar: Button
    private lateinit var txtOlvideContrasena: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {
                v, insets -> val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar el presentador
        presentador = clsLoginPresentador(this)

        // Vincular controles del XML
        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnIngresar = findViewById(R.id.btnIngresar)
        txtOlvideContrasena = findViewById(R.id.txtOlvideContrasena)

        // Abrir la pantalla de recuperación, llevando el identificador si ya se escribió
        txtOlvideContrasena.setOnClickListener {
            val intent = Intent(this, RecuperarPasswordActivity::class.java)
            intent.putExtra(
                RecuperarPasswordActivity.EXTRA_IDENTIFICADOR,
                txtUsuario.text.toString().trim()
            )
            startActivity(intent)
        }

        // Evento del botón
        btnIngresar.setOnClickListener {
            val identificador = txtUsuario.text.toString().trim()
            val clave = txtContrasena.text.toString()

            btnIngresar.isEnabled = false
            presentador.realizarLogin(identificador, clave)
        }
    }

    // Mostrar errores enviados por el Presentador
    fun mostrarError(mensaje: String) {
        btnIngresar.isEnabled = true

        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
    }

    // Resultado exitoso enviado por el Presentador

    fun loginExitoso(mensaje: String, usuario: UsuarioLogin?) {
        btnIngresar.isEnabled = true

        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()

        val intent = Intent(this,MainActivity::class.java)

        // Nombre y tipo para mostrarlos de inmediato en la pantalla de inicio
        intent.putExtra(MainActivity.EXTRA_NOMBRE, usuario?.nombre)
        intent.putExtra(MainActivity.EXTRA_TIPO, usuario?.tipo)

        startActivity(intent)

        finish()
    }
}
