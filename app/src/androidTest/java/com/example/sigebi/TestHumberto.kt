package com.example.sigebi

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sigebi.Modelo.clsLibrosModelo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestHumberto {

    private val modeloLibros = clsLibrosModelo()

    // PRUEBA 1: CONSULTAR CATALOGO
    @Test
    fun consultarCatalogo() = runBlocking {

        val libros = modeloLibros.consultarLibros()

        println("CATALOGO CONSULTADO")
        println("Total de libros: ${libros.size}")

        for (libro in libros) {
            println("Titulo: ${libro.titulo}")
        }

        assertTrue("El catalogo esta vacio", libros.isNotEmpty())
    }

    // PRUEBA 2: BUSCAR POR TITULO
    @Test
    fun buscarPorTitulo() = runBlocking {

        val libros = modeloLibros.buscarLibros("Principito")

        println("BUSQUEDA POR TITULO")
        println("Resultados: ${libros.size}")

        for (libro in libros) {
            println("Titulo: ${libro.titulo}")
            println("Autor: ${libro.autor}")
        }

        assertTrue(
            "No se encontro El Principito",
            libros.any {
                it.titulo.contains("Principito", ignoreCase = true)
            }
        )
    }

    // PRUEBA 3: BUSCAR POR AUTOR
    @Test
    fun buscarPorAutor() = runBlocking {

        val libros = modeloLibros.buscarLibros("Gabriel")

        println("BUSQUEDA POR AUTOR")

        for (libro in libros) {
            println("Titulo: ${libro.titulo}")
            println("Autor: ${libro.autor}")
        }

        assertTrue(
            "No se encontraron libros del autor",
            libros.any {
                it.autor?.contains("Gabriel", ignoreCase = true) == true
            }
        )
    }

    // PRUEBA 4: LIBRO INEXISTENTE
    @Test
    fun buscarLibroInexistente() = runBlocking {

        val libros = modeloLibros.buscarLibros(
            "LibroInexistente123456"
        )

        println("RESULTADOS: ${libros.size}")

        assertTrue(
            "La busqueda deberia estar vacia",
            libros.isEmpty()
        )
    }

    // PRUEBA 5: VALIDAR DATOS JSON
    @Test
    fun validarMapeoJSON() = runBlocking {

        val libros = modeloLibros.buscarLibros("Principito")

        assertTrue(libros.isNotEmpty())

        val libro = libros.first()

        assertTrue(libro.idLibro > 0)
        assertTrue(libro.titulo.isNotBlank())
        assertTrue(libro.isbn.isNotBlank())

        println("MAPEO JSON CORRECTO")
        println("ID: ${libro.idLibro}")
        println("Titulo: ${libro.titulo}")
        println("ISBN: ${libro.isbn}")
        println("Autor: ${libro.autor}")
    }
}

