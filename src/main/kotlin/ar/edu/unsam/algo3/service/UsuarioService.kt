package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.entity.dto.FiguritaDTO
import ar.edu.unsam.algo3.entity.dto.UsuarioDTO
import ar.edu.unsam.algo3.entity.dto.UsuarioFormDTO
import ar.edu.unsam.algo3.repository.Entidad
import ar.edu.unsam.algo3.repository.Repositorio
import ar.edu.unsam.algo3.repository.RepositorioFiguritas
import ar.edu.unsam.algo3.repository.RepositorioUsuarios
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import figuritas.Figurita
import figuritas.LoginException
import figuritas.TipoDeUsuario
import figuritas.Usuario
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Date
import javax.management.monitor.StringMonitor

@Service
class UsuarioService(
    val repositorioUsuarios: RepositorioUsuarios
) {

    fun getInfoUsuario(idUsuario: Int): Usuario {
        return repositorioUsuarios.getById(idUsuario)
    }

    fun getInfoUsuarioForm(idUsuario: Int): Usuario {
        return repositorioUsuarios.getById(idUsuario)
    }

    fun getUsuarioById(idUsuario: Int) = repositorioUsuarios.getById(idUsuario)

    fun actualizarUsuario(idUsuario: Int, usuarioActualizado : UsuarioFormDTO) {
        val usuario = repositorioUsuarios.getById(idUsuario)
        return repositorioUsuarios.update(JSONtoUser(usuario, usuarioActualizado))
    }

    fun verifyPassword(idUsuario: Int, password: String) = repositorioUsuarios.getPasswordById(idUsuario) == password

    fun getUsuarios() : List<Usuario> = repositorioUsuarios.objetos

    fun getFiguritasRepetidasOtrosUsuarios(idUsuario: Int) = repositorioUsuarios.getFiguritasFromOtherUsers(idUsuario)

    fun verifyUser(user: String, password: String): Int {
        // Buscar el usuario por nombre de usuario
        val usuario = repositorioUsuarios.getUserByName(user)
        if (repositorioUsuarios.getPasswordById(usuario.id) != password) throw LoginException(user)
        return usuario.id
    }

    fun getFiguritasFromOtherUsers(idUsuario: Int) : Map<Usuario, List<Figurita>>{
        return repositorioUsuarios.getFiguritasFromOtherUsers(idUsuario)
    }

    fun getFiguritasRegalablesFromOtherUsers(idUsuario: Int): Map<Usuario, List<Figurita>> {
        val otherUsers = getOtherUsers(idUsuario)
        val  mapaFigus = mutableMapOf<Usuario, List<Figurita>>()
        otherUsers.forEach { mapaFigus.put(it, it.figuritasQuePuedeRegalar()) }
        return mapaFigus
    }

    fun getOtherUsers(idUsuario: Int): List<Usuario> = repositorioUsuarios.getOtherUsers(idUsuario)

    fun getFiguritasFaltantes(idUsuario: Int): List<Figurita> = getUsuarioById(idUsuario).figuritasFaltantes

    fun getFiguritasFaltantes(): List<Figurita> =  repositorioUsuarios.getUsuarios().flatMap { it.figuritasFaltantes }

    fun getFiguritasRepetidas(idUsuario: Int): List<Figurita> = getUsuarioById(idUsuario).figuritasRepetidas

    fun getFiguritasRegalables(): List<Figurita> = repositorioUsuarios.getUsuarios().flatMap { it.figuritasQuePuedeRegalar() }

    fun verificarFiguEnUsuario(figurita: Figurita): Boolean {
        return repositorioUsuarios.objetos.any { usuario -> usuario.figuritasFaltantes.contains(figurita) || usuario.figuritasRepetidas.contains(figurita) }
    }

    fun usuariosActivos(): List<Usuario> = repositorioUsuarios.getUsuarios().filter { esUsuarioActivo(it) }

    fun esUsuarioActivo(usuario: Usuario) = !usuario.albumLleno() || !usuario.figuritasRepetidas.isEmpty()

}

data class UsuarioJSON(
    val nombre: String,
    val apellido: String,
    val correoElectronico: String,
    val fechaDeNacimiento: LocalDate,
    val provincia: String,
    val calle: String,
    val localidad: String,
    val altura: Int,
    val criterio: String
)

fun JSONtoUser(usuario: Usuario ,usuarioJSON: UsuarioFormDTO): Usuario{
    usuario.nombre = usuarioJSON.nombre
    usuario.apellido = usuarioJSON.apellido
    usuario.email = usuarioJSON.correoElectronico
    usuario.fechaDeNacimiento = usuarioJSON.fechaDeNacimiento
    usuario.direccion.provincia = usuarioJSON.provincia
    usuario.direccion.calle = usuarioJSON.calle
    usuario.direccion.localidad = usuarioJSON.localidad
    usuario.direccion.altura = usuarioJSON.altura
    return usuario
}