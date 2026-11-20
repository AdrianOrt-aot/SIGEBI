package com.example.sigebi.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Respuesta de resumen_usuario.php (pantalla de inicio / MainActivity)
@Serializable
data class dataRespuestaResumen(
    val ok: Boolean = false,
    val mensaje: String? = null,
    val usuario: dataUsuarioResumen? = null,
    val estadisticas: dataEstadisticas? = null,
    val prestamos: List<dataPrestamoActivo> = emptyList(),
    val novedades: List<dataLibro> = emptyList()
)

@Serializable
data class dataUsuarioResumen(
    val identificador: String,
    @SerialName("Nombre") val nombre: String,
    @SerialName("ApellidoP") val apellidoP: String,
    @SerialName("ApellidoM") val apellidoM: String? = null,
    @SerialName("Correo") val correo: String? = null,
    @SerialName("Area") val area: String? = null,
    // Grupo del alumno o puesto del bibliotecario
    @SerialName("Detalle") val detalle: String? = null,
    val tipo: String
)

@Serializable
data class dataEstadisticas(
    val prestamosActivos: Int = 0,
    val prestamosVencidos: Int = 0,
    val multasPendientes: Int = 0,
    val montoMultas: Double = 0.0,
    val totalLibros: Int = 0
)

@Serializable
data class dataPrestamoActivo(
    @SerialName("ID_Detalle") val idDetalle: Int,
    @SerialName("ID_Libro") val idLibro: Int,
    @SerialName("Titulo") val titulo: String,
    @SerialName("Autor") val autor: String? = null,
    @SerialName("URL_Imagen") val urlImagen: String? = null,
    @SerialName("FechaPrestamo") val fechaPrestamo: String,
    @SerialName("FechaDevolucion") val fechaDevolucion: String? = null,
    @SerialName("Vencido") val vencido: Boolean = false
)
