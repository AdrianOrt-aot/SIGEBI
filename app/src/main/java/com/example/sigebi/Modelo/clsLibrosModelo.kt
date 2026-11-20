package com.example.sigebi.Modelo

import com.example.sigebi.Red.RetrofitClient

class clsLibrosModelo {
    // Consultar todos los libros
    suspend fun consultarLibros(): List<dataLibro> {
        return RetrofitClient.apiService.consultarLibros().libros
    }

    // Buscar libros por título, autor o ISBN
    suspend fun buscarLibros(texto: String): List<dataLibro> {
        return RetrofitClient.apiService.buscarLibros(texto).libros
    }
}