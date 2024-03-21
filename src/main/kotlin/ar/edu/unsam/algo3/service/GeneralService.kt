package ar.edu.unsam.algo3.service

import figuritas.Usuario
import org.springframework.stereotype.Service

@Service
class GeneralService (
    val usuarioService: UsuarioService,
    val figuritaService: FiguritaService,
    val puntoDeVentaService: PuntoDeVentaService,
    val jugadorService: JugadorService
){
    fun getUsuarioActivos(): List<Usuario> = usuarioService.usuariosActivos()

    fun getCantidadUsuariosActivos() = getUsuarioActivos().count()

    fun getCantidadFiguritasRegalables(): Int = figuritaService.getFiguritasRegalables().count()

    fun getCantidadFiguritasFaltantes(): Int = figuritaService.getFiguritasFaltantes().count()

    fun getCantidadPuntosDeVenta(): Int = puntoDeVentaService.getPuntosDeVenta().count()

    fun getPosiciones() = jugadorService.getPosiciones().map{ it::class.java.simpleName }

}