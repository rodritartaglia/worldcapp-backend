package figuritas

import ar.edu.unsam.algo3.repository.Entidad
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class Jugador(
    var nombre : String = "nombre",
    var apellido : String = "apellido",
    var fechaDeNacimiento: LocalDate = LocalDate.now(),
    var nroCamiseta : Int = 1,
    var esLider : Boolean = true,
    var seleccion : Seleccion = Seleccion(),
    val anioDebut : LocalDate = LocalDate.now(),
    var altura : Double = 1.70,
    var peso : Double = 65.0,
    var cotizacion : Double = 50.0,
    var posicion : Posicion = Arquero,
    override var id: Int = 0,
    ) : Entidad
{

    fun edad() = fechaDeNacimiento.comparoAlPresente()

    fun pais() = seleccion.toString().take(3)

    fun valoracion() = posicion.valoracion(this)

    fun esAlto() = altura >= 1.80

    fun esLigero()= peso in 65.0..70.0

    fun esCampeonDelMundo() = seleccion.esCampeonaMundial()

    fun aniosDesdeDebut() = anioDebut.comparoAlPresente().toDouble()

    fun copasGanadas() = seleccion.cantidadDeCopasTotales()

    fun seleccionTieneCopasPar() = seleccion.tieneCopasPar()

    fun esLeyenda() = muchosAniosEnSeleccion() && ((cotizacionAlta() || nroCamisetaImportante()) && esLider)

    fun esPromesa() = esJoven() && cotizacionBaja() && pocosAniosDeDebut()

    fun esJoven() = edad() <= 22

    fun pocosAniosDeDebut() = aniosDesdeDebut() < 2.0

    fun nroCamisetaImportante() = nroCamiseta in 5..10

    fun camisetaPar() = nroCamiseta.esPar()

    fun muchosAniosEnSeleccion()= aniosDesdeDebut() > 10

    fun cotizacionAlta() = cotizacion > 20.0

    fun cotizacionBaja() = cotizacion <= 20.0

    override fun condicionDeBusqueda(value: String) = nombre.contains(value) || apellido.contains(value)

    override fun esValido(): Boolean {
        return nombre.isNotBlank() &&
                apellido.isNotBlank() &&
                nroCamiseta in 1..99 &&
                seleccion.esValido() &&
                altura > 0 &&
                peso > 0 &&
                cotizacion > 0
    }
}


interface Posicion {
    var valorPiso : Double

    fun valoracion(jugador: Jugador) : Double =
        if (condicionPosicion(jugador)) valorPiso + bonusPosicion(jugador)
        else valorPiso

    fun bonusPosicion(jugador : Jugador): Double

    fun condicionPosicion(jugador : Jugador): Boolean

}

object Arquero : Posicion{
    override var valorPiso = 100.0
    override fun bonusPosicion(jugador : Jugador) = (valorPiso * jugador.altura) - valorPiso
    override fun condicionPosicion(jugador: Jugador) = jugador.esAlto()
}

object Defensor : Posicion{
    override var valorPiso = 50.0
    override fun bonusPosicion(jugador : Jugador) = (jugador.aniosDesdeDebut() * 10)
    override fun condicionPosicion(jugador: Jugador) = jugador.esLider
}

object Mediocampista : Posicion{
    override var valorPiso = 150.0
    override fun bonusPosicion(jugador: Jugador) = jugador.peso
    override fun condicionPosicion(jugador: Jugador) = jugador.esLigero()
}

object Delantero : Posicion{
    override var valorPiso = 200.0
    override fun bonusPosicion(jugador: Jugador) = (jugador.nroCamiseta * 10.0)
    override fun condicionPosicion(jugador: Jugador) = jugador.esCampeonDelMundo()
}

class Polivalente(var posiciones : MutableList<Posicion> = mutableListOf()) : Posicion{
    override var valorPiso = promedioPorPosiciones(sumaDeValoresPiso())
    override fun bonusPosicion(jugador: Jugador) = promedioPorPosiciones(sumaDeValoracionPosiciones(jugador)) - jugador.edad().toDouble()
    override fun condicionPosicion(jugador: Jugador) = jugador.esLeyenda() || jugador.esPromesa()

    fun promedioPorPosiciones(valoracion: Double) = valoracion/posiciones.size

    fun sumaDeValoresPiso() = posiciones.sumOf { it.valorPiso }
    fun sumaDeValoracionPosiciones(jugador: Jugador) =  posiciones.sumOf{it.valoracion(jugador)}
}