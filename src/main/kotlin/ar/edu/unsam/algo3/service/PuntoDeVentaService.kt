package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.entity.dto.PuntoDeVentaDTO
import ar.edu.unsam.algo3.repository.RepositorioPuntoDeVenta
import figuritas.*
import org.springframework.stereotype.Service
import org.uqbar.geodds.Point

@Service
class PuntoDeVentaService(
    val repositorioPuntoDeVenta: RepositorioPuntoDeVenta,
    val usuarioService: UsuarioService
)
{
    fun getPuntosDeVenta() : List<PuntoDeVenta>{
        return repositorioPuntoDeVenta.objetos
    }

    fun getUsario(idUsuario: Int): Usuario = usuarioService.getUsuarioById(idUsuario)

    fun buscarPuntoDeVenta(idUsuario: Int, busqueda: String?): Map<Usuario, List<PuntoDeVenta>> {
        if (busqueda == null) {
            return mapOf(getUsario(idUsuario) to buscarPuntoDeVenta(""))
        }
        return mapOf(getUsario(idUsuario) to buscarPuntoDeVenta(busqueda))
    }

    fun getPuntosDeVentaRespectoAUsuario(idUsuario: Int) = mapOf( getUsario(idUsuario) to getPuntosDeVenta())

    fun buscarPuntoDeVenta(busqueda: String): List<PuntoDeVenta> {
        val parametroLowerCase = busqueda.lowercase()
        return repositorioPuntoDeVenta.search(parametroLowerCase)
    }

    fun agregarPuntoDeVenta(puntoDeVentaDTO: PuntoDeVentaDTO) {
        if (puntoDeVentaDTO.tipo == "Kiosco"){
            val puntoDeVentaNuevo = Kiosco(
                nombre = puntoDeVentaDTO.nombre
            ).apply {
                direccion = Direccion(calle = puntoDeVentaDTO.calle, altura = puntoDeVentaDTO.altura, ubicacionGeografica = Point(puntoDeVentaDTO.coordenadaX, puntoDeVentaDTO.coordenadaY));
            stockActual = puntoDeVentaDTO.stock}
            repositorioPuntoDeVenta.create(puntoDeVentaNuevo)
        }
        else if (puntoDeVentaDTO.tipo == "Libreria"){
            val puntoDeVentaNuevo = Libreria(
                nombre = puntoDeVentaDTO.nombre
            ).apply {
                direccion = Direccion(calle = puntoDeVentaDTO.calle, altura = puntoDeVentaDTO.altura, ubicacionGeografica = Point(puntoDeVentaDTO.coordenadaX, puntoDeVentaDTO.coordenadaY));
                stockActual = puntoDeVentaDTO.stock}
            repositorioPuntoDeVenta.create(puntoDeVentaNuevo)
        }
        else{
            val puntoDeVentaNuevo = Supermercado(
                nombre = puntoDeVentaDTO.nombre
            ).apply {
                direccion = Direccion(calle = puntoDeVentaDTO.calle, altura = puntoDeVentaDTO.altura, ubicacionGeografica = Point(puntoDeVentaDTO.coordenadaX, puntoDeVentaDTO.coordenadaY));
                stockActual = puntoDeVentaDTO.stock
            }
            repositorioPuntoDeVenta.create(puntoDeVentaNuevo)
        }
    }

    fun eliminarPuntoDeVenta(idPuntoDeVenta: Int){
        val puntoDeVenta = repositorioPuntoDeVenta.getById(idPuntoDeVenta)
        repositorioPuntoDeVenta.delete(puntoDeVenta)
    }

    fun modificarPuntoDeVenta(puntoDeVentaDTO: PuntoDeVentaDTO){
        val puntoDeVenta = repositorioPuntoDeVenta.getById(puntoDeVentaDTO.id)
        val puntoDeVentaActualizado = updatePuntoDeVenta(puntoDeVenta, puntoDeVentaDTO)
        repositorioPuntoDeVenta.update(puntoDeVentaActualizado)
    }

    fun updatePuntoDeVenta(puntoDeVenta: PuntoDeVenta, puntoDeVentaDTO: PuntoDeVentaDTO) : PuntoDeVenta{
        puntoDeVenta.nombre = puntoDeVentaDTO.nombre
        puntoDeVenta.direccion.calle = puntoDeVentaDTO.calle
        puntoDeVenta.direccion.altura = puntoDeVentaDTO.altura
        puntoDeVenta.stockActual = puntoDeVentaDTO.stock
        puntoDeVenta.direccion.ubicacionGeografica = Point(puntoDeVentaDTO.coordenadaX, puntoDeVentaDTO.coordenadaY)
        return puntoDeVenta
    }

    fun tipos() = mutableListOf(
        Kiosco().nombre,
        Libreria().nombre,
        Supermercado().nombre
    )

}