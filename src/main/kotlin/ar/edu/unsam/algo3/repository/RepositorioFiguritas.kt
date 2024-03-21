package ar.edu.unsam.algo3.repository

import figuritas.Figurita
import figuritas.Jugador
import org.springframework.stereotype.Repository

@Repository
class RepositorioFiguritas : Repositorio<Figurita>() {

    fun figuritaContiene(jugador:Jugador) = objetos.any{ figurita:Figurita -> figurita.jugador == jugador }

    fun getFiguritas() = objetos

}