package com.example.sigebi.Presentador

import com.example.sigebi.InterfazApi.ApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class clsPerfilPresentador(
    private var view: ApiService.View?,
    private val apiService: ApiService,
    private val scope: CoroutineScope
) : ApiService.Presenter {

    private var jobActual: Job? = null

    override fun cargarPerfil() {

        val currentView = view ?: return

        // Cancelar consulta anterior
        jobActual?.cancel()

        currentView.mostrarCargando(true)

        jobActual = scope.launch {

            try {

                val response = apiService.obtenerPerfil()

                val activeView = view ?: return@launch

                activeView.mostrarCargando(false)

                if (response.isSuccessful) {

                    val body = response.body()

                    if (
                        body?.status.equals(
                            "success",
                            ignoreCase = true
                        ) && body?.data != null
                    ) {

                        activeView.mostrarPerfil(body.data)

                    } else {

                        activeView.mostrarError(
                            body?.message
                                ?: "No se pudo obtener el perfil"
                        )
                    }

                } else {

                    val mensaje = when (response.code()) {

                        401 -> "Sesión no autorizada (401)"

                        403 -> "Acceso denegado (403)"

                        404 -> "No se encontró el perfil (404)"

                        500 -> "Error interno del servidor (500)"

                        else ->
                            "Error del servidor: ${response.code()}"
                    }

                    activeView.mostrarError(mensaje)
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                val activeView = view ?: return@launch

                activeView.mostrarCargando(false)

                activeView.mostrarError(
                    "Error de conexión: " +
                            (e.localizedMessage ?: "sin detalle")
                )
            }
        }
    }

    override fun onDestroy() {

        jobActual?.cancel()

        jobActual = null

        view = null
    }
}