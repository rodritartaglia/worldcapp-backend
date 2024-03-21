package ar.edu.unsam.algo3.repository


import figuritas.Jugador
import org.springframework.stereotype.Repository

@Repository
class RepositorioJugadores(): Repositorio<Jugador>() {

    fun getJugadores() = objetos

}