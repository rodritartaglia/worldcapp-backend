package ar.edu.unsam.algo3.repository

import figuritas.Confederacion
import figuritas.Seleccion
import org.springframework.stereotype.Repository

@Repository
class RepositorioSelecciones: Repositorio<Seleccion>() {

    fun getSelecciones() = objetos

    fun getConfederaciones() = Confederacion.values()

}