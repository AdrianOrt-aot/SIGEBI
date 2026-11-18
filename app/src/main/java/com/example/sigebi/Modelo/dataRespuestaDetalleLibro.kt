package com.example.sigebi.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataRespuestaDetalleLibro(
    val status: String,
    val message: String,
    val data: dataDetalleLibro? = null
)