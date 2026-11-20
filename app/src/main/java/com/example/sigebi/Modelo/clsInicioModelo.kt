package com.example.sigebi.Modelo

import com.example.sigebi.Red.RetrofitClient
import retrofit2.Response

class clsInicioModelo {
    // Datos del usuario, estadísticas, préstamos activos y novedades del catálogo
    suspend fun obtenerResumen(): Response<dataRespuestaResumen> {
        return RetrofitClient.apiService.obtenerResumen()
    }
}
