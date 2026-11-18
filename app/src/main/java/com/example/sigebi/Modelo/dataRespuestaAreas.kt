package com.example.sigebi.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataRespuestaAreas(
    val status: String,
    val message: String,
    val data: List<dataAreas> = emptyList()
)