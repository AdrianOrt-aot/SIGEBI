package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataAreas(
    @SerialName("ID_Area") val idArea: Int,
    @SerialName("Nombre") val nombre: String
) {
    override fun toString(): String {
        return nombre
    }
}