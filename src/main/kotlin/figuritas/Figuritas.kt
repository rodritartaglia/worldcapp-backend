package figuritas

import ar.edu.unsam.algo3.entity.dto.FiguritaDTO
import ar.edu.unsam.algo3.repository.Entidad

class Figurita(
    var numero: Int = 1,
    var onFire: Boolean = false,
    var nivelDeImpresion: NivelImpresion = NivelImpresion.ALTA,
    var jugador: Jugador = Jugador(),
    var urlImagen: String = "",
    override var id: Int = 0
) : Entidad {

    companion object{
        val VALOR_PISO : Double = 100.0
        val FACTOR_ONFIRE: Double = 1.2
    }

    fun stringNumeroFigurita() = numero.toString()

    fun valoracion() = valorBase() + jugador.valoracion()

    fun valorBase() : Double = VALOR_PISO * bonusPorEstarOnFire() * bonusPorSerPar() * bonusPorNivelDeImpresion()

    fun bonusPorEstarOnFire() : Double = if (onFire) FACTOR_ONFIRE else 1.0

    fun bonusPorSerPar() : Double = if (laFiguritaEsPar()) 1.1 else 1.0

    fun laFiguritaEsPar() = numero.esPar()

    fun jugadorCamisetaPar() = jugador.camisetaPar()

    fun jugadorEsLeyenda() = jugador.esLeyenda()

    fun jugadorEsPromesa() = jugador.esPromesa()

    fun seleccion()= jugador.seleccion

    fun stringSeleccion() = seleccion().pais.toString()

    fun seleccionCopasPar() = jugador.seleccionTieneCopasPar()

    fun bonusPorNivelDeImpresion() = if (esNivelDeImpresion(NivelImpresion.BAJA)) 1.0 else 0.85

    fun esNivelDeImpresion(nivel: NivelImpresion) = this.nivelDeImpresion == nivel

    fun impresionAlta() = nivelDeImpresion == NivelImpresion.ALTA

    fun nombreJugador() = jugador.nombre

    fun apellidoJugador() = jugador.apellido

    fun nombreCompletoJugador() = nombreJugador() + " " + apellidoJugador()

    fun emitirDescripcion() = """Numero de figurita: $numero 
                                Valor de la figurita: ${valoracion()}
                                Seleccion de la figurita: ${seleccion().pais}
                                Apellido y nombre del jugador: ${apellidoJugador()}, ${nombreJugador()}"""

    override fun condicionDeBusqueda(value : String) = nombreJugador().contains(value, ignoreCase = true) || apellidoJugador().contains(value, ignoreCase = true) || nombreCompletoJugador().contains(value, ignoreCase = true) ||  stringNumeroFigurita().equals(value) || stringSeleccion().contains(value, ignoreCase = true)


    override fun esValido(): Boolean {
        return numero > 0 && jugador.esValido()
    }

}

enum class NivelImpresion{
    BAJA, MEDIA, ALTA
}
