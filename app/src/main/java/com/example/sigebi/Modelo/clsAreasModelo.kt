package com.example.sigebi.Modelo

import com.example.sigebi.Red.RetrofitClient

class clsAreasModelo {
    suspend fun obtenerAreas(): dataRespuestaAreas {
        return RetrofitClient.apiService.obtenerAreas()
    }
}