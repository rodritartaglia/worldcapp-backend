package ar.edu.unsam.algo3.repository

import ar.edu.unsam.algo3.service.SeleccionesService
import ar.edu.unsam.algo3.service.SeleccionesServiceAlgoII
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import figuritas.*
import org.springframework.stereotype.Repository

interface Entidad {
    var id: Int

    fun condicionDeBusqueda(value : String) : Boolean

    fun validarDatos() {
        if (!esValido()) {
            throw InvalidEntityException()
        }
    }

    fun esNuevo() = id == 0

    fun esValido(): Boolean

}

@Repository
class Repositorio<T:Entidad>{
    val objetos: MutableList<T> = mutableListOf()
    var nextId  = 1

    fun create(objeto: T) {
        validarEsNuevo(objeto)
        objeto.validarDatos()
        asignarId(objeto)
        objetos.add(objeto)
    }

    fun asignarId(objeto: T) {
        objeto.id = nextId++
    }

    fun delete(objeto: T) {
        val objetoARemover = getById(objeto.id)
        objetos.remove(objetoARemover)
    }

    fun update(objeto: T){
        objeto.validarDatos()
        val objetoAActualizar = getById(objeto.id)
        val index = objetos.indexOf(objetoAActualizar)
        objetos[index] = objeto
    }

    fun getById(id: Int): T  {
        validarEstaEnColeccion(id)
        return objetos.find { it.id == id }!!
    }

    fun search(value: String): List<T> {
        return objetos.filter { it.condicionDeBusqueda(value) }
    }

    fun isInRepository(id: Int): Boolean = objetos.any { it.id == id }

    fun validarEsNuevo(objeto: T) {
        if (!objeto.esNuevo()) {
            throw EntityAlreadyExistsException()
        }
    }

    fun validarEstaEnColeccion(id: Int) {
        if ( objetos.none { it.id == id } )
            throw EntityNotFoundException(id)
    }

}

class Actualizador(){
    lateinit var repoAActualizar: Repositorio<Seleccion>
    lateinit var service: SeleccionesServiceAlgoII
    val mapper : ObjectMapper = jacksonObjectMapper()

    fun fromJSONtoListSelecciones(): List<Seleccion> = mapper.readValue(service.getSelecciones())

    fun actualizar()=fromJSONtoListSelecciones().forEach { crearOActualizarObjeto(it) }

    fun crearOActualizarObjeto(seleccion: Seleccion) {
        if (seleccion.esNuevo()) {
            repoAActualizar.create(seleccion)
        } else {
            repoAActualizar.update(seleccion)
        }
    }
}