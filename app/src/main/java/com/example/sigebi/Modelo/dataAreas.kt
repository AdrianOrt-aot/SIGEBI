package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataAreas(
    @SerialName("ID_TipoLibro") val idTipoLibro: Int,
    @SerialName("TipoLibro") val tipoLibro: String
) {
    override fun toString(): String {
        return tipoLibro
    }
}