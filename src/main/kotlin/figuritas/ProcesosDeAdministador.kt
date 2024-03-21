package figuritas

import ar.edu.unsam.algo3.repository.Actualizador
import ar.edu.unsam.algo3.repository.Repositorio

//class Administrador(mail: String) {
//    fun ejecutarProcesos(procesos: MutableList<ProcesoDeAdministrador>) = procesos.forEach { it.iniciarEjecucion() }
//}

abstract class ProcesoDeAdministrador() {
    open lateinit var tipoProceso: String
    lateinit var mailSender: MailSender

    fun iniciarEjecucion() {
        execute()
        mailSender.sendMail(crearMail())
    }

    abstract fun execute()

    fun crearMail() = Mail(
        origin = "sarasa",
        to = "admin@worldcapp.com.ar",
        title = "¡Felicitaciones!",
        body = "Se realizo el proceso <${tipoProceso}>"
        )

}

class BorrarUsuarioInactivo(
    val usuariosDeLaAplicacion: Repositorio<Usuario>,
    override var tipoProceso: String = "BorrarUsuarioInactivo"
): ProcesoDeAdministrador()
{
    override fun execute() {
       usuariosInactivos().forEach { usuariosDeLaAplicacion.delete(it) }
    }

    fun usuariosInactivos() = usuariosDeLaAplicacion.objetos.filter { esUsuarioInactivo(it) }

    fun esUsuarioInactivo(usuario: Usuario) = usuario.albumLleno() && usuario.figuritasRepetidas.isEmpty()

}

object CreacionOActualizacionDeSelecciones : ProcesoDeAdministrador() {

    lateinit var actualizador: Actualizador
    override var tipoProceso: String = "CreacionOActualizacionDeSelecciones"


    override fun execute() {
        actualizador.actualizar()
    }

}

class CambiarEstadoOnFire(
    val loteDeFiguritas: Repositorio<Figurita>,
    override var tipoProceso: String = "cambiarEstadoOnFire"
): ProcesoDeAdministrador()
{
    override fun execute() {
        loteDeFiguritas.objetos.forEach{ it.onFire = true }
    }

}

class BorarPuntoDeVenta(
    val puntosDeVenta: Repositorio<PuntoDeVenta>,
    override var tipoProceso: String = "BorrarPuntoDeVenta"
) : ProcesoDeAdministrador()
{
    companion object {
        val DIAS_MAXIMOS_PERMITIDOS = 90
    }

    override fun execute() {
        puntosDeVentaInactivos().forEach { puntosDeVenta.delete(it) }
    }

    fun puntosDeVentaInactivos() = puntosDeVenta.objetos.filter { esPuntoDeVentaInactivo(it) }

    fun esPuntoDeVentaInactivo(puntoDeVenta: PuntoDeVenta): Boolean = !puntoDeVenta.disponibilidad() && pedidosAFabrica(puntoDeVenta)

    fun pedidosAFabrica(puntoDeVenta: PuntoDeVenta): Boolean =
        if (puntoDeVenta.pedidosPendientes().isEmpty()) {
            true
        } else {
            puntoDeVenta.pedidosPendientes().all { it.cuantosDiasQuedanParaElIngreso() > DIAS_MAXIMOS_PERMITIDOS }
        }

}

class ActualizarStockPuntosDeVenta(
    val puntosDeVenta: Repositorio<PuntoDeVenta>,
    override var tipoProceso: String = "ActualizarStockPuntosDeVenta"
): ProcesoDeAdministrador(){

    override fun execute() {
        puntosDeVenta.objetos.forEach { it.actualizar() }
    }

}
