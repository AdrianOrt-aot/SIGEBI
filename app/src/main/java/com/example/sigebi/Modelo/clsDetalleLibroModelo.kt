package com.example.sigebi.Modelo

import com.example.sigebi.Red.RetrofitClient

class clsDetalleLibroModelo {
    suspend fun obtenerDetalleLibro(idLibro: Int): dataRespuestaDetalleLibro {
        return RetrofitClient.apiService.detalleLibro(idLibro)
    }
}