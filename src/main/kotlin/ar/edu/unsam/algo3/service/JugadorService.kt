package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.entity.dto.JugadorDTO
import ar.edu.unsam.algo3.repository.RepositorioJugadores
import figuritas.*
import org.springframework.stereotype.Service

@Service
class JugadorService(
    val repositorioJugadores: RepositorioJugadores,
    val seleccionesService: SeleccionesService
) {

    fun agregarJugador(jugadorDTO: JugadorDTO) {
        val jugadorNuevo = Jugador(
            nombre = jugadorDTO.primerNombre,
            apellido = jugadorDTO.apellido,
            fechaDeNacimiento = jugadorDTO.fechaDeNacimiento,
            altura = jugadorDTO.altura,
            peso = jugadorDTO.peso,
            nroCamiseta = jugadorDTO.numeroDeCamiseta,
            seleccion = seleccionesService.getSeleccion(jugadorDTO.seleccion.id),
            esLider = jugadorDTO.esLider,
            cotizacion = jugadorDTO.cotizacion,
            posicion = getPosicionByString(jugadorDTO)
        )
        repositorioJugadores.create(jugadorNuevo)
    }

    fun modificarJugador(jugadorDTO: JugadorDTO){
        val jugador = getJugador(jugadorDTO.id)
        val jugadorActualizado = updateJugador(jugador, jugadorDTO)
        repositorioJugadores.update(jugadorActualizado)
    }

    private fun updateJugador(jugador : Jugador, jugadorDTO: JugadorDTO): Jugador {
        jugador.nombre = jugadorDTO.primerNombre
        jugador.apellido = jugadorDTO.apellido
        jugador.fechaDeNacimiento = jugadorDTO.fechaDeNacimiento
        jugador.altura = jugadorDTO.altura
        jugador.peso = jugadorDTO.peso
        jugador.nroCamiseta = jugadorDTO.numeroDeCamiseta
        jugador.seleccion = seleccionesService.getSeleccion(jugadorDTO.seleccion.id)
        jugador.esLider = jugadorDTO.esLider
        jugador.cotizacion = jugadorDTO.cotizacion
        jugador.posicion = getPosicionByString(jugadorDTO)

        return jugador
    }

    fun getJugadores(): List<Jugador> = repositorioJugadores.getJugadores()

    fun getJugador(id: Int) : Jugador = repositorioJugadores.getById(id)

    fun borrarJugador(jugador: Jugador) = repositorioJugadores.delete(jugador)

    fun getPosiciones(): MutableList<Posicion> {
        return mutableListOf(
            Arquero,
            Defensor,
            Mediocampista,
            Delantero
        )
    }

    fun getPosicionByString(jugadorDTO : JugadorDTO) : Posicion {
        return when (jugadorDTO.posicion) {
            "Arquero" -> Arquero
            "Defensor" -> Defensor
            "Mediocampista" -> Mediocampista
            else -> Delantero
        }
    }

    fun buscar(search: String) = repositorioJugadores.search(search)

}