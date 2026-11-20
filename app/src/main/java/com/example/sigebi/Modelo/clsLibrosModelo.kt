package com.example.sigebi.Modelo

import com.example.sigebi.Red.RetrofitClient

class clsLibrosModelo {

    // Consultar libros con ejemplares disponibles
    suspend fun consultarLibros(): List<dataLibro> {
        return RetrofitClient.apiService.consultarLibros().libros.filter { (it.disponibles ?: 0) > 0 }
    }

    // Buscar únicamente libros disponibles
    suspend fun buscarLibros(texto: String): List<dataLibro> {
        return RetrofitClient.apiService.buscarLibros(texto).libros
    }
}