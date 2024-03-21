package figuritas
import ar.edu.unsam.algo3.repository.Entidad
import org.uqbar.geodds.Point
import java.time.LocalDate

class Usuario (
    override var id: Int = 0,
    var nombre: String = "nombre",
    var apellido: String = "apellido",
    val userName: String = "userName",
    val password: String = "password",
    var email: String = "email",
    var avatarUrl: String = "assets/fotoDefault.png",
    val observersSolicitud: MutableList<SolicitudObserver> = mutableListOf(),
    var fechaDeNacimiento: LocalDate = LocalDate.now(),

    val direccion: Direccion = Direccion(),
    var cercaniaRelativa: Double = 50000.0,

    var jugadorFavorito: Jugador = Jugador(),

    val figuritasRepetidas: MutableList<Figurita> = mutableListOf(),
    val figuritasFaltantes: MutableList<Figurita> = mutableListOf(),

    var tipoDeUsuario: TipoDeUsuario = Conservador,
) : Entidad
{
    companion object{
        val FACTOR_POCAS_FIGURITAS = 5
    }

    fun edad() = fechaDeNacimiento.comparoAlPresente()

    fun estaCercaDeOtroUsuario(usuario: Usuario) = direccion.calcularCercania(usuario.direccion) <= cercaniaRelativa

    fun albumLleno() = figuritasFaltantes.isEmpty()
    //agregar y sacar figu
    fun agregarFiguritaRepetida(figurita: Figurita)=figuritasRepetidas.add(figurita)

    fun agregarFiguritaFaltante(figurita: Figurita)=figuritasFaltantes.add(figurita)

    fun sacarFiguritaRepetida(figurita: Figurita)=figuritasRepetidas.remove(figurita)

    fun sacarFiguritaFaltante(figurita: Figurita)=figuritasFaltantes.remove(figurita)

    //Para validación de trade
    fun leFaltaLaFigu(figurita: Figurita) = figurita in figuritasFaltantes

    fun puedeRegalarEsaFigu(figurita: Figurita) = figuritasQuePuedeRegalar().contains(figurita)

    fun validaQueFalteFigu(figurita: Figurita){
        if (!leFaltaLaFigu(figurita))
            throw UserAlreadyHasFiguritaException()
    }

    fun validaQuePuedaRegalarla(figurita: Figurita){
        if(!puedeRegalarEsaFigu(figurita))
            throw FiguritaCantBeGivenException()
    }

    fun validaQueEsteCerca(usuario: Usuario){
        if(!estaCercaDeOtroUsuario(usuario))
            throw TargetIsTooFarException()
    }

    fun validaCambiarFigurita(proveedor:Usuario,figurita:Figurita) {
        validaQueFalteFigu(figurita)
        proveedor.validaQuePuedaRegalarla(figurita)
        validaQueEsteCerca(proveedor)
    }

    fun tradear(proveedor:Usuario,figurita: Figurita){
        sacarFiguritaFaltante(figurita)
        proveedor.sacarFiguritaRepetida(figurita)
    }

    fun cambiarFigurita(proveedor: Usuario, figurita: Figurita) {
        validaCambiarFigurita(proveedor, figurita)
        tradear(proveedor, figurita)
        notificarSolicitud(figurita)
    }

    fun notificarSolicitud(figurita: Figurita) {
        observersSolicitud.toList().forEach { it.notificarSolicitudExitosa(this, figurita) }
    }

    fun aniadirAccion(accion: SolicitudObserver){
        observersSolicitud.add(accion)
    }

    fun eliminarAccion(accion: SolicitudObserver){
        observersSolicitud.remove(accion)
    }

    fun tieneFiguritaTop(figurita: Figurita):Boolean = figurita in mejoresCinco()

    fun mejoresCinco():List<Figurita> = mejoresSinRepetir().take(5)

    fun mejoresSinRepetir():List<Figurita> = figuritasRepetidas.toSet().sortedByDescending { figurita -> figurita.valoracion() }

    fun figuritasQuePuedeRegalar(): List<Figurita> = figuritasRepetidas.filter { puedeRegalar(it) }

    fun figuritasQuePuedeRegalar(listaDeFiguritas: MutableList<Figurita>): List<Figurita> = listaDeFiguritas.filter { puedeRegalar(it) }

    fun puedeRegalar(figurita: Figurita): Boolean = tipoDeUsuario.puedeRegalar(figurita,this)

    fun laFiguEsElFavorito(figurita:Figurita)=(figurita.jugador == jugadorFavorito)

    fun leFaltanPocasFigus() = figuritasFaltantes.size <= FACTOR_POCAS_FIGURITAS

    fun noTieneRepetidas() = figuritasRepetidas.isEmpty()

    fun puedeRegalarMasQue(cantidad: Int) = figuritasQuePuedeRegalar().size > cantidad

    override fun condicionDeBusqueda(value : String) = nombre.contains(value) || apellido.contains(value) || userName == value

    override fun esValido() : Boolean {
        return nombre.isNotBlank() &&
                apellido.isNotBlank() &&
//                userName.isNotBlank() &&
                email.isNotBlank() &&
                direccion.esValido()
    }
}


class Direccion (
    var provincia: String = "provincia",
    var localidad: String = "localidad",
    var calle: String = "calle",
    var altura: Int = 1111,
    var ubicacionGeografica: Point = Point(0,0)
){
    fun calcularCercania(direccionObjetivo: Direccion): Double = ubicacionGeografica.distance(direccionObjetivo.ubicacionGeografica)

    fun esValido(): Boolean {
        return calle.isNotBlank() &&
                altura > 0
                //provincia.isNotBlank() &&
                //localidad.isNotBlank() &&

    }

    fun getCalleConAltura() : String = calle + " ${altura}"

    fun getUbicacionGeografica(): String = "(${ubicacionGeografica.x}; ${ubicacionGeografica.y})"

}

interface TipoDeUsuario {
    fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean
}

object Par : TipoDeUsuario {
    override fun puedeRegalar(figurita: Figurita, usuario: Usuario):Boolean = !(figurita.laFiguritaEsPar() || figurita.jugadorCamisetaPar() || figurita.seleccionCopasPar())
}

class Nacionalista(
    var seleccionesQueridas: MutableSet<Seleccion> = mutableSetOf()
): TipoDeUsuario {
    override fun puedeRegalar(figurita: Figurita, usuario: Usuario):Boolean = !(seleccionesQueridas.contains(figurita.seleccion()))
}

object Conservador : TipoDeUsuario {

    override fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean = figurita.impresionAlta() && usuario.albumLleno()

}

object Fanatico : TipoDeUsuario {
    override fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean =  !usuario.laFiguEsElFavorito(figurita) && !figurita.jugadorEsLeyenda()
}

object Desprendido : TipoDeUsuario {
    override fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean = true

}

object Apostador : TipoDeUsuario {

    override fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean = !(figurita.onFire || figurita.jugadorEsPromesa())
}

object Interesado : TipoDeUsuario {
    override fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean = !usuario.tieneFiguritaTop(figurita)
}

object Cambiante : TipoDeUsuario {

    override fun puedeRegalar(figurita: Figurita, usuario: Usuario): Boolean = tipoUsuarioActual(usuario).puedeRegalar(figurita,usuario)

    fun tipoUsuarioActual(usuario: Usuario) = if (usuario.edad() < 25) Desprendido else Conservador
}

