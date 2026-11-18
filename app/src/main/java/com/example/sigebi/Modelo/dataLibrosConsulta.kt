package com.example.sigebi.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataLibrosConsulta(
    val exito: Boolean = false,

    val total: Int = 0,

    val libros: List<dataLibro> = emptyList(),

    val mensaje: String = ""
)
