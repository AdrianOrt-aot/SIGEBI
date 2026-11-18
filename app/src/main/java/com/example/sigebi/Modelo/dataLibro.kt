package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataLibro(
    @SerialName("ID_Libro")
    val idLibro: Int,

    @SerialName("Titulo")
    val titulo: String,

    @SerialName("ISBN")
    val isbn: String,

    @SerialName("Folio")
    val folio: String,

    @SerialName("TipoLibro")
    val tipoLibro: String,

    @SerialName("Editorial")
    val editorial: String,

    @SerialName("Autor")
    val autor: String? = null,

    @SerialName("TotalEjemplares")
    val totalEjemplares: Int? = null,

    @SerialName("Disponibles")
    val disponibles: Int? = null
)
