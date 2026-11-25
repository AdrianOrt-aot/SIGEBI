package com.example.sigebi.InterfazApi

import com.example.sigebi.Modelo.dataLibrosConsulta
import com.example.sigebi.Modelo.PerfilRespons
import retrofit2.Response
import com.example.sigebi.Modelo.dataLoginRequest
import com.example.sigebi.Modelo.dataLoginResponse
import com.example.sigebi.Modelo.dataPerfilUsuario
import com.example.sigebi.Modelo.dataRespuestaApi
import com.example.sigebi.Modelo.dataRespuestaAreas
import com.example.sigebi.Modelo.dataRespuestaDetalleLibro
import com.example.sigebi.Modelo.dataRespuestaResumen
import com.example.sigebi.Modelo.dataRestablecerPasswordRequest
import com.example.sigebi.Modelo.dataSolicitarRecuperacionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // Consultar catálogo de libros
    @GET("consultar_libros.php")
    suspend fun consultarLibros(): dataLibrosConsulta

    // Buscar por título, autor o ISBN
    @GET("buscar_libros.php")
    suspend fun buscarLibros(
        @Query("buscar") buscar: String
    ): dataLibrosConsulta

    //perfil de autenticado
    @GET("obtener_Perfil.php")
    suspend fun obtenerPerfil(): Response<PerfilRespons>
    @POST("login.php")
    suspend fun iniciarSesion(
        @Body request: dataLoginRequest
    ): Response<dataLoginResponse>

    // Obtener detalle de un libro
    @GET("detalle_libro.php")
    suspend fun detalleLibro(
        @Query("idLibro") idLibro: Int
    ): dataRespuestaDetalleLibro

    // Obtener todas las areas
    @GET("obtener_areas.php")
    suspend fun obtenerAreas(): dataRespuestaAreas

    // Contrato de la vista del perfil
    interface View {

        fun mostrarCargando(mostrar: Boolean)

        fun mostrarPerfil(perfil: dataPerfilUsuario)

        fun mostrarError(mensaje: String)
    }

    // Contrato del presentador del perfil
    interface Presenter {

        fun cargarPerfil()

        fun onDestroy()
    }

    // Datos de la pantalla de inicio (requiere sesión)
    @GET("resumen_usuario.php")
    suspend fun obtenerResumen(): Response<dataRespuestaResumen>

    // Recuperar contraseña: paso 1, envía el correo con el enlace y el código
    @POST("solicitar_recuperacion.php")
    suspend fun solicitarRecuperacion(
        @Body request: dataSolicitarRecuperacionRequest
    ): Response<dataRespuestaApi>

    // Recuperar contraseña: paso 2, guarda la nueva contraseña con el código recibido
    @POST("restablecer_password.php")
    suspend fun restablecerPassword(
        @Body request: dataRestablecerPasswordRequest
    ): Response<dataRespuestaApi>
}