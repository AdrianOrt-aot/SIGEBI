package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataSolicitudPrestamo(
    @SerialName("ID_Solicitud")
    val idSolicitud: Int,

    @SerialName("ID_Libro")
    val idLibro: Int,

    @SerialName("ID_Ejemplar")
    val idEjemplar: Int,

    @SerialName("Estado")
    val estado: String,

    @SerialName("FechaSolicitud")
    val fechaSolicitud: String,

    @SerialName("Titulo")
    val titulo: String,

    @SerialName("URL_Imagen")
    val urlImagen: String? = null
)

@Serializable
data class dataRespuestaSolicitudes(
    val ok: Boolean = false,
    val mensaje: String? = null,
    val data: List<dataSolicitudPrestamo> = emptyList()
)

@Serializable
data class dataPeticionSolicitud(
    @SerialName("id_libro")
    val idLibro: Int,
)

@Serializable
data class dataResultadoSolicitud(
    val ok: Boolean = false,
    val mensaje: String? = null
)

@Serializable
data class dataTokenCsrf(
    val ok: Boolean = false,

    @SerialName("csrf_token")
    val token: String? = null,

    val mensaje: String? = null
)
