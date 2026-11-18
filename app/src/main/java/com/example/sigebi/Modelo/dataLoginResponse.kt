package com.example.sigebi.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataLoginResponse(
    val ok: Boolean,
    val mensaje: String? = null,
    val usuario: UsuarioLogin? = null
)

@Serializable
data class UsuarioLogin(
    val identificador: String,
    val nombre: String? = null,
    val tipo: String? = null,
    val puesto: String? = null
)
