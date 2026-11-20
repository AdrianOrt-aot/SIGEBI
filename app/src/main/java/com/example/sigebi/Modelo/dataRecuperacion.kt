package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Petición a solicitar_recuperacion.php
// tipo: "ALUMNO" (matrícula) o "TRABAJADOR" (número de control)
@Serializable
data class dataSolicitarRecuperacionRequest(
    val tipo: String,
    val identificador: String
)

// Petición a restablecer_password.php
@Serializable
data class dataRestablecerPasswordRequest(
    val tipo: String,
    val identificador: String,
    val token: String,
    @SerialName("password_nueva") val passwordNueva: String
)

// Respuesta genérica {ok, mensaje} de las APIs
@Serializable
data class dataRespuestaApi(
    val ok: Boolean = false,
    val mensaje: String? = null
)
