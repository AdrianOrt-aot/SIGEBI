package com.example.sigebi.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataLoginRequest(
    val identificador: String,
    val password: String
)
