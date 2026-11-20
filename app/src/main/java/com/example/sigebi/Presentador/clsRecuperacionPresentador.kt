package com.example.sigebi.Presentador

import com.example.sigebi.Modelo.clsRecuperacionModelo
import com.example.sigebi.Red.RetrofitClient
import com.example.sigebi.Vista.RecuperarPasswordActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Presentador de la recuperación de contraseña.
// Usa las APIs solicitar_recuperacion.php y restablecer_password.php (tabla tblrecuperacionpassword).
class clsRecuperacionPresentador(private val vista: RecuperarPasswordActivity) {

    private val modelo = clsRecuperacionModelo()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Paso 1: pedir que se envíe el correo con el enlace y el código
    fun solicitarRecuperacion(tipo: String, identificador: String) {
        val id = identificador.trim()
        if (id.isEmpty() || id.length > 10) {
            vista.mostrarError("Escribe tu matrícula o número de control (máximo 10 caracteres).")
            return
        }

        vista.mostrarCargando(true)
        scope.launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    modelo.solicitarRecuperacion(tipo, id)
                }
                if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                    vista.solicitudEnviada(
                        respuesta.body()?.mensaje
                            ?: "Si la cuenta tiene correo registrado, recibirás instrucciones."
                    )
                } else {
                    vista.mostrarError(
                        RetrofitClient.mensajeDeError(respuesta) ?: when (respuesta.code()) {
                            503 -> "El servicio de recuperación no está disponible por ahora."
                            else -> "No se pudo enviar la solicitud (HTTP ${respuesta.code()})."
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + (e.localizedMessage ?: "desconocido"))
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }

    // Paso 2: guardar la nueva contraseña usando el código recibido por correo
    fun restablecerPassword(
        tipo: String,
        identificador: String,
        codigo: String,
        nueva: String,
        confirmacion: String
    ) {
        val id = identificador.trim()
        val token = codigo.trim().lowercase()

        when {
            id.isEmpty() || id.length > 10 ->
                return vista.mostrarError("Escribe tu matrícula o número de control.")
            !Regex("^[0-9a-f]{64}$").matches(token) ->
                return vista.mostrarError("El código no es válido. Cópialo completo desde el correo.")
            nueva.length < 12 ->
                return vista.mostrarError("La nueva contraseña debe tener al menos 12 caracteres.")
            nueva != confirmacion ->
                return vista.mostrarError("Las contraseñas no coinciden.")
        }

        vista.mostrarCargando(true)
        scope.launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    modelo.restablecerPassword(tipo, id, token, nueva)
                }
                if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                    vista.passwordRestablecida(
                        respuesta.body()?.mensaje ?: "Contraseña restablecida correctamente"
                    )
                } else {
                    vista.mostrarError(
                        RetrofitClient.mensajeDeError(respuesta)
                            ?: "No se pudo restablecer la contraseña (HTTP ${respuesta.code()})."
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + (e.localizedMessage ?: "desconocido"))
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }

    fun detener() {
        scope.cancel()
    }
}
