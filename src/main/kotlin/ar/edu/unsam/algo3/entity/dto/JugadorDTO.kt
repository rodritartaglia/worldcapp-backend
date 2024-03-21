package ar.edu.unsam.algo3.entity.dto

import figuritas.Jugador
import figuritas.Posicion
import figuritas.Seleccion
import java.time.LocalDate

class JugadorDTO(
    val nombre: String,
    val primerNombre : String,
    val apellido : String,
    val fechaDeNacimiento: LocalDate,
    val numeroDeCamiseta: Int,
    val seleccion: SeleccionDTO,
    val posicion: String,
    val altura: Double,
    val peso: Double,
    val cotizacion: Double,
    val id: Int,
    val esLider : Boolean
){

   constructor(jugador:Jugador): this(
       nombre = jugador.nombre + " " + jugador.apellido,
       primerNombre = jugador.nombre,
       apellido = jugador.apellido,
       fechaDeNacimiento = jugador.fechaDeNacimiento,
       numeroDeCamiseta = jugador.nroCamiseta,
       seleccion = SeleccionDTO(jugador.seleccion),
       posicion = jugador.posicion::class.java.simpleName,
       altura = jugador.altura,
       peso = jugador.peso,
       cotizacion = jugador.cotizacion,
       id = jugador.id,
       esLider = jugador.esLider
   )

}