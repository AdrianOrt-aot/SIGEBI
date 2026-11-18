package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataEjemplar(
    @SerialName("ID_Ejemplar") val idEjemplar: Int,
    @SerialName("ISBNEjemplar") val isbnEjemplar: String,
    @SerialName("ID_Estado") val idEstado: Int,
    @SerialName("Estado") val estado: String,
    @SerialName("Disponible") val disponible: Boolean
)