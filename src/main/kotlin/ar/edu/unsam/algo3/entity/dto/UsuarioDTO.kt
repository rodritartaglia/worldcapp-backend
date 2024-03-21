package ar.edu.unsam.algo3.entity.dto

import figuritas.Usuario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class UsuarioDTO(
    val username: String,
    val nombreCompleto: String,
    val fechaNacimiento: String,
    val edad: Int,
    val ubicacion: String,
    val avatarUrl: String,
) {
    constructor(usuario: Usuario) : this(
        usuario.userName,
        "${usuario.nombre} ${usuario.apellido}",
        usuario.fechaDeNacimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        usuario.edad().toInt(),
        usuario.direccion.localidad,
        usuario.avatarUrl
    )

}

data class UsuarioFormDTO(
    var id: Int,
    var nombre: String,
    var apellido: String,
    var correoElectronico: String,
    var fechaDeNacimiento: LocalDate,
    var provincia: String,
    var localidad: String,
    var calle : String,
    var altura: Int
) {
    constructor(usuario:Usuario): this(
        id = usuario.id,
        nombre = usuario.nombre,
        apellido = usuario.apellido,
        correoElectronico = usuario.email,
        fechaDeNacimiento = usuario.fechaDeNacimiento,
        calle = usuario.direccion.calle,
        provincia = usuario.direccion.provincia,
        localidad = usuario.direccion.localidad,
        altura = usuario.direccion.altura
    )
}

data class UsuarioCedeFiguritaDTO(
    val id: Int,
    val nombreUsuario: String
){
    constructor(usuario: Usuario): this(
        id = usuario.id,
        nombreUsuario = "${usuario.nombre} ${usuario.apellido}"
    )
}

fun Usuario.toCedeFiguritaDTO() = UsuarioCedeFiguritaDTO(this)

