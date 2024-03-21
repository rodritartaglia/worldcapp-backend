package ar.edu.unsam.algo3.entity.dto

import figuritas.Figurita
import figuritas.NivelImpresion
import figuritas.Usuario
import java.time.LocalDate

class FiguritaDTO(
    var id: Int?,
    var numero: Int,
    var nombre:String,
    var valoracion: Double,
    var peso: Double,
    var altura: Double,
    var onFire: Boolean,
    var promesa: Boolean,
    var nroCamiseta: Int,
    var fechaDeNacimiento: LocalDate,
    var seleccion: String,
    var posicion: String,
    var cotizacion: Double,
    var debut: Int,
    val confederacion: String,
    val nivelDeImpresion: NivelImpresion,
    val valoracionBase: Double,
    val esLider: Boolean,
    var cedidaPor: UsuarioCedeFiguritaDTO?
){
    constructor(figurita:Figurita): this(
        id= figurita.id,
        numero= figurita.numero,
        nombre= "${figurita.nombreJugador()} ${figurita.apellidoJugador()}",
        valoracion= figurita.valoracion(),
        peso= figurita.jugador.peso,
        altura= figurita.jugador.altura,
        onFire= figurita.onFire,
        promesa= figurita.jugadorEsPromesa(),
        nroCamiseta= figurita.jugador.nroCamiseta,
        fechaDeNacimiento= figurita.jugador.fechaDeNacimiento,
        seleccion= figurita.seleccion().pais,
        posicion= figurita.jugador.posicion::class.java.simpleName,
        cotizacion= figurita.jugador.cotizacion,
        debut = figurita.jugador.anioDebut.year,
        confederacion = figurita.jugador.seleccion.confederacion.toString(),
        nivelDeImpresion = figurita.nivelDeImpresion,
        valoracionBase = figurita.valorBase(),
        esLider = figurita.jugador.esLider,
        cedidaPor = null
    )

    constructor(figurita: Figurita, usuario: Usuario): this(figurita){
        cedidaPor = UsuarioCedeFiguritaDTO(usuario)
    }

}