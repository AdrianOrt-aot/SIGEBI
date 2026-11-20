package com.example.sigebi.Modelo

import com.example.sigebi.Red.RetrofitClient
import retrofit2.Response

class clsRecuperacionModelo {
    suspend fun solicitarRecuperacion(tipo: String, identificador: String): Response<dataRespuestaApi> {
        return RetrofitClient.apiService.solicitarRecuperacion(
            dataSolicitarRecuperacionRequest(tipo = tipo, identificador = identificador)
        )
    }

    suspend fun restablecerPassword(
        tipo: String,
        identificador: String,
        token: String,
        passwordNueva: String
    ): Response<dataRespuestaApi> {
        return RetrofitClient.apiService.restablecerPassword(
            dataRestablecerPasswordRequest(
                tipo = tipo,
                identificador = identificador,
                token = token,
                passwordNueva = passwordNueva
            )
        )
    }
}
