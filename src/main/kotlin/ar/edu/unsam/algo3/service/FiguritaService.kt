package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.entity.dto.FiguritaDTO
import ar.edu.unsam.algo3.repository.RepositorioFiguritas
import ar.edu.unsam.algo3.repository.RepositorioJugadores
import ar.edu.unsam.algo3.repository.RepositorioUsuarios
import figuritas.CantBeDeletedException
import figuritas.Figurita
import figuritas.Jugador
import figuritas.Usuario
import org.springframework.stereotype.Service

@Service
class FiguritaService(
    val usuarioService: UsuarioService,
    val repositorioFiguritas: RepositorioFiguritas,
    val jugadorService: JugadorService,
)
{
    fun getInfoFigurita(idFigurita: Int, idUsuario: Int): Map<Usuario, Figurita>{
        return mapOf<Usuario,Figurita>( usuarioService.getUsuarioById(idUsuario) to getFigurita(idFigurita))
    }

    fun getFigurita(idFigurita: Int) : Figurita = repositorioFiguritas.getById(idFigurita)

    fun getUsuario(idUsuario: Int): Usuario = usuarioService.getUsuarioById(idUsuario)

    fun getFiguritasRegalables() : List<Figurita> = usuarioService.getFiguritasRegalables()

    fun getFiguritasSolicitablesForUsuarios(
        rangoMin: Int?,
        rangoMax: Int?,
        onFire: Boolean,
        promesa: Boolean,
        idUsuario: Int,
        busqueda:String?) : Map<Usuario, List<Figurita>> {

        var figuritasFaltantes = usuarioService.getFiguritasFaltantes(idUsuario).filter {
                figurita -> condicionOnFire(onFire, figurita) && condicionPromesa(promesa, figurita) }

        if (rangoMin != null && rangoMax != null){
            figuritasFaltantes = figuritasFaltantes.filter{condicionRango(rangoMin, rangoMax, it)}
        }

        var figuritasRegalablesFromOtherUsers = usuarioService.getFiguritasRegalablesFromOtherUsers(idUsuario)

        if (busqueda == null){
            return figuritasRegalablesFromOtherUsers.mapValues { (usuario, figuritas) -> figuritas.filter { figuritasFaltantes.contains(it) } }
        }
        return figuritasRegalablesFromOtherUsers.mapValues { (usuario, figuritas) -> figuritas.filter { figuritasFaltantes.contains(it) && buscarFigurita(busqueda).contains(it) } }
    }

    fun condicionOnFire(onFire : Boolean, figurita: Figurita) : Boolean{
        return if (!onFire) true
        else figurita.onFire
    }

    fun condicionPromesa(promesa : Boolean, figurita : Figurita) : Boolean{
        return if (!promesa) true
        else figurita.jugadorEsPromesa()
    }

    fun buscarFigurita(parametro: String) : List<Figurita>{
        val parametroLowerCase = parametro.lowercase()
        return repositorioFiguritas.search(parametroLowerCase)
    }

    fun getFiguritasRepetidasOtrosUsuarios(idUsuario: Int) = usuarioService.getFiguritasRepetidasOtrosUsuarios(idUsuario)

    fun condicionRango(rangoMin: Int?, rangoMax: Int?, figurita: Figurita): Boolean {
        return if (rangoMax === null && rangoMin !== null) figurita.valoracion() >= rangoMin
        else if (rangoMin === null && rangoMax !== null) figurita.valoracion() <= rangoMax
        else if (rangoMin === null && rangoMax === null) true
        else figurita.valoracion() >= rangoMin!! && figurita.valoracion() <= rangoMax!!
    }

    fun isInRepository(idFigurita: Int) : Boolean{
        return repositorioFiguritas.isInRepository(idFigurita)
    }

    //TRAIDO DE USUARIO SERVICE

    fun getFiguritasRepetidas(idUsuario: Int): List<Figurita> = usuarioService.getFiguritasRepetidas(idUsuario)


    fun getFiguritasFaltantes(idUsuario: Int): List<Figurita> = usuarioService.getFiguritasFaltantes(idUsuario)

    fun getFiguritasFaltantes(): List<Figurita> = usuarioService.getFiguritasFaltantes()

    fun borrarFiguritaFaltante(idUsuario: Int, idFigurita: Int){
        val figuritaABorrar : Figurita = getFigurita(idFigurita)
        usuarioService.getUsuarioById(idUsuario).sacarFiguritaFaltante(figuritaABorrar)
    }

    fun agregarFiguritaFaltante(idUsuario: Int, idFigurita: Int){
        if ( isInRepository(idFigurita) ) {
            val figuritaFaltante = getFigurita(idFigurita)
            usuarioService.getUsuarioById(idUsuario).agregarFiguritaFaltante(figuritaFaltante)
        }
    }
    fun agregarFiguritaRepetida(idUsuario: Int, idFigurita: Int){
        if ( isInRepository(idFigurita) ) {
            val figuritaRepetida = getFigurita(idFigurita)
            usuarioService.getUsuarioById(idUsuario).agregarFiguritaRepetida(figuritaRepetida)
        }
    }

    fun borrarFiguritaRepetida(idUsuario: Int, idFigurita: Int){
        val figuritaABorrar : Figurita = getFigurita(idFigurita)
        usuarioService.getUsuarioById(idUsuario).sacarFiguritaRepetida(figuritaABorrar)
    }

    fun solicitarFigurita(idUsuario: Int, idDuenioFigurita: Int, idFigurita: Int) {
        val usuario = usuarioService.getUsuarioById(idUsuario)
        val duenio = usuarioService.getUsuarioById(idDuenioFigurita)
        val figurita = getFigurita(idFigurita)
        usuario.cambiarFigurita(duenio, figurita)
    }


    fun existeJugadorEnFiguritas(jugador: Jugador): Boolean = repositorioFiguritas.objetos.any{ figurita:Figurita -> figurita.jugador == jugador }

    fun esFavoritoDeAlgunUsuario(jugador: Jugador): Boolean = usuarioService.getUsuarios().any{ it.jugadorFavorito == jugador }

    fun getAllFiguritas() : List<Figurita> {
       return repositorioFiguritas.objetos
    }

    fun crearFigurita(figurita : Figurita) {
        val jugadorr = jugadorService.getJugador(figurita.jugador.id)
        val figuritaACrear = Figurita(numero = figurita.numero, onFire = figurita.onFire, nivelDeImpresion = figurita.nivelDeImpresion, jugador = jugadorr)
        repositorioFiguritas.create(figuritaACrear)
    }

    fun eliminarFigurita(id: Int) {
        val figuritaABorrar = repositorioFiguritas.getById(id)
        if(!usuarioService.verificarFiguEnUsuario(figuritaABorrar)) {
            repositorioFiguritas.delete(figuritaABorrar)
        } else {
            throw Exception("No se pudo eliminar la figurita")
        }

    }

    fun modificarFigu(patchContent: Figurita) {
        val updateJugador = jugadorService.getJugador(patchContent.jugador.id)
        patchContent.jugador = updateJugador
        repositorioFiguritas.update(patchContent)
    }

    fun eliminarJugador(idJugador: Int): Unit {
        val jugador = jugadorService.getJugador(idJugador)
        if (existeJugadorEnFiguritas(jugador) || esFavoritoDeAlgunUsuario(jugador)){
            val reason = mutableListOf<String>()
            if (existeJugadorEnFiguritas(jugador)){
                reason.add("El jugador existe en una figurita")
            }
            if (esFavoritoDeAlgunUsuario(jugador)){
                reason.add("El jugador es favorito de un usuario")
            }

            throw CantBeDeletedException(reason)
        }
        return jugadorService.borrarJugador(jugador)

    }




}

