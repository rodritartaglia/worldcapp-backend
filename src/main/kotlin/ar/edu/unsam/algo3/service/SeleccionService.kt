package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.repository.RepositorioSelecciones
import figuritas.Seleccion
import org.springframework.stereotype.Service

@Service
class SeleccionesService
    (val repositorioSelecciones: RepositorioSelecciones){

    fun getNombres() = repositorioSelecciones.getSelecciones().map { it.pais }
    fun getConfederaciones() = repositorioSelecciones.getConfederaciones()
    fun agregarSeleccion(seleccionJSON: Seleccion) {
        repositorioSelecciones.create(seleccionJSON)
    }

    fun modificarSeleccion(seleccionJSON: Seleccion) {
        repositorioSelecciones.update(seleccionJSON)
    }

    fun borrarSeleccion(id: Int) {
        val seleccion = repositorioSelecciones.getById(id)
        repositorioSelecciones.delete(seleccion)
     }

    fun getSelecciones() = repositorioSelecciones.getSelecciones()

    fun getSeleccion(id: Int) = repositorioSelecciones.getById(id)

}

interface SeleccionesServiceAlgoII {

    fun getSelecciones(): String

}