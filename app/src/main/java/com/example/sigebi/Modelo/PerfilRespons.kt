package com.example.sigebi.Modelo
import kotlinx.serialization.Serializable

@Serializable
data class PerfilRespons(
    val status: String,
    val message: String,
    val data: PerfilUsuario? = null
)

@Serializable
data class PerfilUsuario(
    val identificador: String,
    val Nombre: String,
    val ApellidoP: String,
    val ApellidoM: String? = null,
    val Telefono: String? = null,
    val Correo: String? = null,
    val Ciudad: String,
    val CP: String,
    val Colonia: String,
    val No_Exterior: String,
    val No_Interior: String? = null,
    val Area: String? = null,
    val tipo: String,
    val NombreGrupo: String? = null,
    val Cuatrimestre: Int? = null,
    val Puesto: String? = null
)