package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataDetalleLibro(
    @SerialName("ID_Libro") val idLibro: Int,
    @SerialName("Titulo") val titulo: String,
    @SerialName("ISBN") val isbn: String,
    @SerialName("Folio") val folio: String,
    @SerialName("ID_TipoLibro") val idTipoLibro: Int,
    @SerialName("TipoLibro") val tipoLibro: String,
    @SerialName("ID_Editorial") val idEditorial: Int,
    @SerialName("Editorial") val editorial: String,
    @SerialName("Autor") val autor: String? = null,
    @SerialName("TotalEjemplares") val totalEjemplares: Int,
    @SerialName("Disponibles") val disponibles: Int,
    @SerialName("Ejemplares") val ejemplares: List<dataEjemplar> = emptyList(),
    @SerialName("URL_Imagen") val urlImagen: String? = null
)