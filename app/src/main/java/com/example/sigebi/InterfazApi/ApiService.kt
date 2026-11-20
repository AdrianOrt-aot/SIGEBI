package com.example.sigebi.InterfazApi

import com.example.sigebi.Modelo.dataLibrosConsulta
import com.example.sigebi.Modelo.PerfilRespons
import com.example.sigebi.Modelo.PerfilUsuario
import com.example.sigebi.Modelo.dataLoginRequest
import com.example.sigebi.Modelo.dataLoginResponse
import com.example.sigebi.Modelo.dataRespuestaAreas
import com.example.sigebi.Modelo.dataRespuestaDetalleLibro

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Consultar catálogo de libros
    @GET("consultar_libros.php")
    suspend fun consultarLibros(): dataLibrosConsulta

    // Buscar libros por título, autor o ISBN
    @GET("buscar_libros.php")
    suspend fun buscarLibros(
        @Query("buscar") buscar: String
    ): dataLibrosConsulta

    // Obtener perfil del usuario autenticado
    @GET("obtener_Perfil.php")
    suspend fun obtenerPerfil(): Response<PerfilRespons>

    // Iniciar sesión
    @POST("login.php")
    suspend fun iniciarSesion(
        @Body request: dataLoginRequest
    ): Response<dataLoginResponse>

    // Obtener detalle de un libro
    @GET("detalle_libro.php")
    suspend fun detalleLibro(
        @Query("idLibro") idLibro: Int
    ): dataRespuestaDetalleLibro

    // Obtener todas las áreas
    @GET("obtener_areas.php")
    suspend fun obtenerAreas(): dataRespuestaAreas

    // Contrato de la vista del perfil
    interface View {

        fun mostrarCargando(mostrar: Boolean)

        fun mostrarPerfil(perfil: PerfilUsuario)

        fun mostrarError(mensaje: String)
    }

    // Contrato del presentador del perfil
    interface Presenter {

        fun cargarPerfil()

        fun onDestroy()
    }
}