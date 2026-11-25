package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataPerfilUsuario(
    @SerialName("id")
    val id: Int,

    @SerialName("nombre")
    val nombre: String,

    @SerialName("correo")
    val correo: String,

    @SerialName("rol")
    val rol: String? = null
)