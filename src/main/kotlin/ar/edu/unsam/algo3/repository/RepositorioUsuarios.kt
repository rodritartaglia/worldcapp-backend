package ar.edu.unsam.algo3.repository

import figuritas.Figurita
import figuritas.LoginException
import figuritas.Usuario
import org.springframework.stereotype.Repository

@Repository
class RepositorioUsuarios: Repositorio<Usuario>() {

    fun getUsuarios() = objetos

    fun getPasswordById(idUsuario: Int) = getById(idUsuario).password

    fun getOtherUsers(idUsuario: Int): List<Usuario> = objetos.filter{ it != getById(idUsuario)}

    fun getFiguritasFromOtherUsers(idUsuario: Int) : Map<Usuario, List<Figurita>> {
        val mapUsers: MutableMap<Usuario, List<Figurita>> = mutableMapOf()
        getOtherUsers(idUsuario).forEach{
            mapUsers.put(it, it.figuritasRepetidas)
        }
        return mapUsers
    }

    fun getUserByName(username: String): Usuario {
        val usuario = objetos.find { it.userName == username } ?: throw LoginException(username)
        return usuario
    }

}