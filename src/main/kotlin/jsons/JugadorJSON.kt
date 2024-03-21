package jsons

import ar.edu.unsam.algo3.entity.dto.SeleccionDTO
import figuritas.Posicion
import java.time.LocalDate

data class JugadorJSON(
    val nombre: String,
    val apellido: String,
    val fechaDeNacimiento: LocalDate,
    val altura: Double,
    val peso: Int,
    val nroCamiseta: Int,
    val seleccion: SeleccionDTO,
    val posicion: Posicion,
    val cotizacion: Int
)