package ar.edu.unsam.algo3.entity.dto

import figuritas.Direccion
import figuritas.PuntoDeVenta
import figuritas.Usuario
import org.uqbar.geodds.Point

data class PuntoDeVentaDTO(
    val id: Int = 0,
    val nombre: String = "",
    val direccion: String = "",
    val calle : String = "",
    val altura : Int = 0,
    val ubicacionGeografica: String = "",
    val distancia: Double = 0.0,
    val stock: Int = 0,
    val pedidosPendientes: Int = 0,
    val precioPorSobre: Double = 0.0,
    val tipo: String = "",
    val coordenadaX: Double = 0.0,
    val coordenadaY: Double = 0.0
){
    constructor( puntoDeVenta: PuntoDeVenta, usuario: Usuario): this(
        id = puntoDeVenta.id,
        nombre = puntoDeVenta.nombre,
        direccion = puntoDeVenta.getDireccion(),
        calle = puntoDeVenta.direccion.calle,
        altura = puntoDeVenta.direccion.altura,
        ubicacionGeografica = puntoDeVenta.getUbicacionGeografica(),
        distancia = puntoDeVenta.direccion.calcularCercania(usuario.direccion),
        stock = puntoDeVenta.stockActual,
        pedidosPendientes = puntoDeVenta.cantidadPedidosPendientes(),
        precioPorSobre = puntoDeVenta.importeTotal(usuario, 1),
        tipo = puntoDeVenta::class.java.simpleName,
        coordenadaX = puntoDeVenta.direccion.ubicacionGeografica.x,
        coordenadaY = puntoDeVenta.direccion.ubicacionGeografica.y
    )

    constructor(puntoDeVenta: PuntoDeVenta): this(
        id = puntoDeVenta.id,
        nombre = puntoDeVenta.nombre,
        direccion = puntoDeVenta.getDireccion(),
        stock = puntoDeVenta.stockActual,
        tipo = puntoDeVenta::class.java.simpleName,
        calle = puntoDeVenta.direccion.calle,
        altura = puntoDeVenta.direccion.altura,
        coordenadaX = puntoDeVenta.direccion.ubicacionGeografica.x,
        coordenadaY = puntoDeVenta.direccion.ubicacionGeografica.y,
        pedidosPendientes = puntoDeVenta.cantidadPedidosPendientes()
    )

}