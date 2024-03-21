package figuritas

import ar.edu.unsam.algo3.repository.Entidad
import com.fasterxml.jackson.annotation.JsonProperty

class Seleccion(
    override var id: Int = 0,
    var pais : String = "Argentina",
    val confederacion : Confederacion = Confederacion.CONMEBOL,
    @JsonProperty("copasDelMundo") var cantidadCopasMundiales : Int = 0,
    @JsonProperty("copasConfederacion") var cantidadCopasConfederacion : Int = 0
) : Entidad
{

    fun esCampeonaMundial() = cantidadCopasMundiales > 0

    fun cantidadDeCopasTotales() = cantidadCopasConfederacion + cantidadCopasMundiales

    fun tieneCopasPar() = cantidadDeCopasTotales().esPar()

    override fun condicionDeBusqueda(value: String) = pais == value

    override fun esValido(): Boolean {
        return pais.isNotBlank() &&
                cantidadCopasMundiales >= 0 &&
                cantidadCopasConfederacion >=0
    }
}

enum class Confederacion(){
    AFC, CAF, CONCACAF, CONMEBOL, OFC, UEFA
}