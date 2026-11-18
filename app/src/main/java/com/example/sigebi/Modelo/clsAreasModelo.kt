package com.example.sigebi.Modelo

class clsAreasModelo {
    suspend fun obtenerAreas(): dataRespuestaAreas {
        return RetrofitClient.apiService.obtenerAreas()
    }
}