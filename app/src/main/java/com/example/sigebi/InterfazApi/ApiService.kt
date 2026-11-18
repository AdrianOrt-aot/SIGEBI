package com.example.sigebi.InterfazApi

import com.example.sigebi.Modelo.dataLibrosConsulta
import com.example.sigebi.Modelo.dataLoginRequest
import com.example.sigebi.Modelo.dataLoginResponse
import retrofit2.Response
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

    @POST("login.php")
    suspend fun iniciarSesion(
        @Body request: dataLoginRequest
    ): Response<dataLoginResponse>
}