package com.example.sigebi.InterfazApi

import com.example.sigebi.Modelo.dataLibrosConsulta
import com.example.sigebi.Modelo.PerfilRespons
import retrofit2.Response
import retrofit2.http.GET
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
}