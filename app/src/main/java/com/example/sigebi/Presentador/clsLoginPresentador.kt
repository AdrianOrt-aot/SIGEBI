package com.example.sigebi.Presentador

import com.example.sigebi.Modelo.clsLoginRepository
import com.example.sigebi.Vista.LoginActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class clsLoginPresentador(private val view: LoginActivity) {

    private val modelo = clsLoginRepository()

    fun realizarLogin(identificador: String, clave: String) {

        if (identificador.isBlank() || clave.isBlank()) {
            view.mostrarError("Por favor, ingresa el identificador y contraseña")

            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    modelo.iniciarSesion(identificador.trim(),clave)
                }

                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()

                    if (datos?.ok == true) {
                        view.loginExitoso(datos.mensaje ?: "Inicio de sesión correcto", datos.usuario)
                    } else {
                        view.mostrarError(datos?.mensaje ?: "No se pudo iniciar sesión")
                    }

                } else {
                    val mensaje = when (respuesta.code()) {

                        400 -> "Datos de acceso inválidos"

                        401 -> "Identificador o contraseña incorrectos"

                        403 -> "Usuario sin autorización"

                        500 -> "Error interno del servidor"

                        else -> "Error HTTP: ${respuesta.code()}"
                    }

                    view.mostrarError(mensaje)
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                view.mostrarError(
                    "Error de conexión o procesamiento: " + (e.localizedMessage ?: "Error desconocido")
                )
            }
        }
    }
}