package com.example.sigebi.Presentador

import com.example.sigebi.Vista.ActivityCatalogo
import com.example.sigebi.Modelo.clsAreasModelo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class clsAreasPresentador(val vista: ActivityCatalogo) {
    private val modelo = clsAreasModelo()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var trabajo: Job? = null

    fun cargarAreas() {
        trabajo = scope.launch {
            try {
                val respuesta = modelo.obtenerAreas()

                if (respuesta.status == "success") {
                    vista.mostrarCategorias(
                        respuesta.data.orEmpty().filter { it.idTipoLibro > 0 }
                    )
                } else {
                    vista.mostrarErrorCategorias(respuesta.message)
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                vista.mostrarErrorCategorias(
                    "Error al consultar las categorías"
                )
            }
        }
    }

    fun detener() {
        trabajo?.cancel()
    }
}