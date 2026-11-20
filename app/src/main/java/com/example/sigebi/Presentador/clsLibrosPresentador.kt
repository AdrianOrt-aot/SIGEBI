package com.example.sigebi.Presentador

import com.example.sigebi.Modelo.clsLibrosModelo
import com.example.sigebi.Vista.ActivityCatalogo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class clsLibrosPresentador(val vista: ActivityCatalogo) {

    private val modeloLibros = clsLibrosModelo()

    private val scope = CoroutineScope(Dispatchers.Main)
    private var trabajoBusqueda: Job? = null

    fun iniciar() {
        cargarLibros()
    }

    fun cargarLibros() {
        trabajoBusqueda?.cancel()
        trabajoBusqueda = scope.launch {

            try {
                val libros = modeloLibros.consultarLibros()
                vista.mostrarLibros(libros)
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                vista.mostrarError("Error al consultar los libros")
            }
        }
    }

    fun buscarLibros(texto: String) {

        trabajoBusqueda?.cancel()

        if (texto.isBlank()) {
            cargarLibros()
            return
        }

        trabajoBusqueda = scope.launch { delay(350)
            try {
                val libros = modeloLibros.buscarLibros(texto.trim())
                vista.mostrarLibros(libros)
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                vista.mostrarError("Error al buscar los libros")
            }
        }
    }

    fun detener() {
        trabajoBusqueda?.cancel()
    }
}
