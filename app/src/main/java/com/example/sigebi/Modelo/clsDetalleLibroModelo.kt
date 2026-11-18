package com.example.sigebi.Modelo

class clsDetalleLibroModelo {
    suspend fun obtenerDetalleLibro(idLibro: Int): dataRespuestaDetalleLibro {
        return RetrofitClient.apiService.detalleLibro(idLibro)
    }
}