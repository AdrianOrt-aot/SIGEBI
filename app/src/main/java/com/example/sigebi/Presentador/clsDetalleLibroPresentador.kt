package com.example.sigebi.Presentador

import com.example.sigebi.Modelo.clsDetalleLibroModelo
import com.example.sigebi.Vista.ActivityDetalleLibro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class clsDetalleLibroPresentador(val vista: ActivityDetalleLibro) {
    private val modelo = clsDetalleLibroModelo()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var trabajo: Job? = null
    fun cargarDetalle(idLibro: Int) {
        trabajo = scope.launch {
            try {
                val respuesta = modelo.obtenerDetalleLibro(idLibro)

                if (respuesta.status == "success" && respuesta.data != null) {
                    vista.mostrarDetalle(respuesta.data)
                } else {
                    vista.mostrarError(respuesta.message)
                }
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                vista.mostrarError("Error al consultar el libro")
            }
        }
    }
    fun detener() {
        trabajo?.cancel()
    }
}