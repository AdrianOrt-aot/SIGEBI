package com.example.sigebi.Presentador

import com.example.sigebi.Modelo.clsInicioModelo
import com.example.sigebi.Red.RetrofitClient
import com.example.sigebi.Vista.MainActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Presentador de la pantalla de inicio: pide a resumen_usuario.php los datos reales del usuario
class clsInicioPresentador(private val vista: MainActivity) {

    private val modelo = clsInicioModelo()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var trabajo: Job? = null

    fun cargarResumen() {
        trabajo?.cancel()
        vista.mostrarCargando(true)

        trabajo = scope.launch {
            try {
                val respuesta = withContext(Dispatchers.IO) { modelo.obtenerResumen() }

                if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                    vista.mostrarResumen(respuesta.body()!!)
                } else if (respuesta.code() == 401) {
                    // La sesión ya no existe en el servidor: regresar al login
                    vista.sesionExpirada()
                } else {
                    vista.mostrarError(
                        RetrofitClient.mensajeDeError(respuesta)
                            ?: "No se pudo cargar tu información (HTTP ${respuesta.code()})"
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
