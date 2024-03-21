package ar.edu.unsam.algo3.repository

import ar.edu.unsam.algo3.entity.dto.PuntoDeVentaDTO
import figuritas.PuntoDeVenta
import figuritas.Usuario
import org.springframework.stereotype.Repository

@Repository
class RepositorioPuntoDeVenta : Repositorio<PuntoDeVenta>() {

    fun getPuntosDeVenta(usuario: Usuario): List<PuntoDeVentaDTO>{
        val puntosDeVentaDTO: MutableList<PuntoDeVentaDTO> = mutableListOf()
        objetos.forEach{ puntosDeVentaDTO.add( PuntoDeVentaDTO(it, usuario) )}
        return puntosDeVentaDTO
    }

}


