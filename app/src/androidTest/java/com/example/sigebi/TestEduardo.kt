package com.example.sigebi

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sigebi.Modelo.clsAreasModelo
import com.example.sigebi.Modelo.clsDetalleLibroModelo
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestEduardo {
    // PRUEBA 1: API DETALLE DEL LIBRO
    @Test
    fun pruebaDetalleLibro() {
        runBlocking {

            val modelo = clsDetalleLibroModelo()

            // Consultamos El Principito con ID 1010
            val respuesta = modelo.obtenerDetalleLibro(1010)

            // Comprobamos que la API respondio correctamente
            assertEquals("success", respuesta.status)

            // Comprobamos que llegaron datos
            assertNotNull(respuesta.data)

            val libro = respuesta.data!!

            // Comprobamos que sea el libro solicitado
            assertEquals(1010, libro.idLibro)
            assertEquals("El Principito", libro.titulo)

            println("PRUEBA DETALLE LIBRO CORRECTA")
            println("ID: ${libro.idLibro}")
            println("Titulo: ${libro.titulo}")
            println("Autor: ${libro.autor}")
            println("Editorial: ${libro.editorial}")
            println("Total ejemplares: ${libro.totalEjemplares}")
            println("Disponibles: ${libro.disponibles}")
        }
    }

    // PRUEBA 2: API OBTENER AREAS
    @Test
    fun pruebaObtenerAreas() {
        runBlocking {

            val modelo = clsAreasModelo()
            val respuesta = modelo.obtenerAreas()

            // Comprobar que la API respondió correctamente
            assertEquals("success", respuesta.status)

            // Obtener la lista de tipos de libros
            val areas = respuesta.data.orEmpty()

            // Comprobar que existen registros
            assertTrue(areas.isNotEmpty())

            println("PRUEBA OBTENER AREAS CORRECTA")

            for (area in areas) {
                println("ID: ${area.idTipoLibro}")
                println("Tipo de libro: ${area.tipoLibro}")
            }
        }
    }
}