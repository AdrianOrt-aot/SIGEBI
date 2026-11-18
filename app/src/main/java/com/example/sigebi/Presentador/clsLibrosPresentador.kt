/*package com.example.sigebi.Presentador

import com.example.sigebi.Modelo.clsLibrosModelo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsLibrosPresentador {
    private val modeloLibros = clsLibrosModelo()

    fun iniciar() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val libros = modeloLibros.consultarLibros()
                vista.mostrarLibros(libros)

            } catch (e: Exception) {
                vista.mostrarError(
                    "Error al cargar los libros: " + e.localizedMessage
                )
            }
        }
    }

    fun buscarLibros(texto: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val libros = modeloLibros.buscarLibros(texto)
                vista.mostrarLibros(libros)

            } catch (e: Exception) {
                vista.mostrarError(
                    "Error al buscar libros: " + e.localizedMessage
                )
            }
        }
    }
}*/