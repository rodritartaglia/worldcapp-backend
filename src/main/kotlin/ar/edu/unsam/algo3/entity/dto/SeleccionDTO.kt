package ar.edu.unsam.algo3.entity.dto

import figuritas.Seleccion

class SeleccionDTO (
    val pais: String,
    val id: Int
){
    constructor(seleccion: Seleccion):this(
        pais = seleccion.pais,
        id = seleccion.id
    ){}
}