package com.example.sigebi.Modelo

import retrofit2.Response

class clsLoginRepository {
    suspend fun iniciarSesion(identificador: String, password: String): Response<dataLoginResponse> {
        val request = dataLoginRequest(identificador = identificador, password = password)

        return RetrofitClient.apiService.iniciarSesion(request)
    }
}