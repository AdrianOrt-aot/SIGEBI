
package com.example.sigebi

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sigebi.Modelo.clsLoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail

@RunWith(AndroidJUnit4::class)
class LoginRetrofitTest {

    @Test
    fun probarConexionLogin() = runBlocking {

        val repository = clsLoginRepository()

        // Credenciales de prueba inválidas
        val respuesta = repository.iniciarSesion(
            identificador = "PRUEBA0001",
            password = "clave_incorrecta_prueba"
        )

        Log.d(
            "LOGIN_TEST",
            "Código HTTP: ${respuesta.code()}"
        )

        if (respuesta.isSuccessful) {

            val datos = respuesta.body()

            Log.d("LOGIN_TEST", "OK: ${datos?.ok}")
            Log.d("LOGIN_TEST", "Mensaje: ${datos?.mensaje}")

        } else {

            Log.d(
                "LOGIN_TEST",
                "Error: ${respuesta.errorBody()?.string()}"
            )
        }

        // El endpoint debe existir
        assertNotEquals(404, respuesta.code())

        // Debe recibirse una respuesta HTTP válida
        // sin error interno del servidor.
        assertTrue(respuesta.code() in 200..499)
    }


    @Test
    fun probarLoginCorrecto() = runBlocking {

        val repository = clsLoginRepository()

        val respuesta = repository.iniciarSesion(
            identificador = "NC001",
            password = "juan123"
        )

        Log.d(
            "LOGIN_TEST",
            "Código HTTP: ${respuesta.code()}"
        )

        if (respuesta.isSuccessful) {

            val datos = respuesta.body()

            Log.d("LOGIN_TEST", "OK: ${datos?.ok}")
            Log.d("LOGIN_TEST", "Mensaje: ${datos?.mensaje}")
            Log.d(
                "LOGIN_TEST",
                "Tipo de usuario: ${datos?.usuario?.tipo}"
            )

            assertNotNull(
                "La respuesta no debe estar vacía",
                datos
            )

            assertTrue(
                "El inicio de sesión debe ser exitoso",
                datos?.ok == true
            )

        } else {

            Log.e(
                "LOGIN_TEST",
                "Error HTTP: ${respuesta.code()}"
            )

            Log.e(
                "LOGIN_TEST",
                "Respuesta: ${respuesta.errorBody()?.string()}"
            )

            fail("El servidor rechazó las credenciales")
        }
    }

}
