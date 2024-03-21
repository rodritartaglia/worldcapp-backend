package figuritas

interface SolicitudObserver {
    fun notificarSolicitudExitosa(usuario: Usuario, figurita: Figurita)

    // tuve que quitar el metodo condicion porque hay un observer que tiene una firma diferente

}

class TriplicarKmObserver(): SolicitudObserver
{
    companion object {
        val VALOR_A_MULTIPLICAR = 3
    }

    override fun notificarSolicitudExitosa(usuario: Usuario, figurita: Figurita) {
        if (condicionAccionAdicional(usuario)) {
            multiplicarKmMaximos(usuario)
            usuario.eliminarAccion(this)
        }
    }

    fun condicionAccionAdicional(usuario: Usuario) = usuario.leFaltanPocasFigus() //pocasFigusFaltantes

    private fun multiplicarKmMaximos(usuario: Usuario) {
        usuario.cercaniaRelativa *= VALOR_A_MULTIPLICAR
    }

}

class cambioANacionalistaObserver(
    var contadorMismaSeleccion: Int = 0,
    var ultimaSeleccionTradeada: Seleccion = Seleccion(),
    ): SolicitudObserver
{
    override fun notificarSolicitudExitosa(usuario: Usuario, figurita: Figurita) {
        sumarContadorMismaSeleccion(figurita)
        if (condicionAccionAdicional(usuario)) {
            usuario.tipoDeUsuario = Nacionalista(mutableSetOf(figurita.seleccion()))
            contadorMismaSeleccion = 0
        }
    }

    fun condicionAccionAdicional(usuario: Usuario) = contadorMismaSeleccion == 3 && !usuario.albumLleno()

    private fun sumarContadorMismaSeleccion(figurita: Figurita) {
        if (ultimaSeleccionTradeada == figurita.seleccion()) {
            ++contadorMismaSeleccion
        } else {
            ultimaSeleccionTradeada = figurita.seleccion()
            contadorMismaSeleccion = 1
        }
    }

}

class RepetidaReservadaMenosValiosaObserver(
    val figuritasRepetidasReservadas: MutableList<Figurita> = mutableListOf()
) : SolicitudObserver
{
    override fun notificarSolicitudExitosa(usuario: Usuario, figurita: Figurita) {
        if(condicionAccionAdicional(usuario, figurita)){
            usuario.figuritasRepetidas.add(figuritaRepetidaRegalableMenosValiosa(usuario))
            figuritasRepetidasReservadas.remove(figuritaRepetidaRegalableMenosValiosa(usuario))
        }
    }

    fun agregarFiguritaReservada(figurita: Figurita) {
        figuritasRepetidasReservadas.add(figurita)
    }

    fun eliminarFiguritaReservada(figurita: Figurita) {
        figuritasRepetidasReservadas.remove(figurita)
    }

    private fun figuritaRepetidaRegalableMenosValiosa(usuario: Usuario): Figurita = figuritasReservadasRegalables(usuario).minBy { it.valoracion() }

    fun condicionAccionAdicional(usuario: Usuario, figurita: Figurita): Boolean {
        return  tieneAlgunaFiguritaDeMayorValoracion(usuario, figurita)
                &&
                usuario.noTieneRepetidas()
    }

    private fun tieneAlgunaFiguritaDeMayorValoracion(usuario: Usuario, figurita: Figurita) = figuritasReservadasRegalables(usuario).any { figuritaReservada -> figurita.valoracion() >= figuritaReservada.valoracion() }

    private fun figuritasReservadasRegalables(usuario: Usuario) = usuario.figuritasQuePuedeRegalar(figuritasRepetidasReservadas)

}

class CambioADesprendidoObserver(
    val cantidadDeFiguritasARegalar: Int
) : SolicitudObserver
{
    override fun notificarSolicitudExitosa(usuario: Usuario, figurita: Figurita) {
        if(condicionAccionAdicional(usuario)){
            usuario.tipoDeUsuario = Desprendido
        }
    }

    fun condicionAccionAdicional(usuario: Usuario): Boolean = usuario.albumLleno() && cantidadMayorDeFiguritasARegalar(usuario)

    private fun cantidadMayorDeFiguritasARegalar(usuario: Usuario) = usuario.puedeRegalarMasQue(cantidadDeFiguritasARegalar)

}

class NotificacionObserver(
    var mailSender: MailSender,
) : SolicitudObserver{

    override fun notificarSolicitudExitosa(usuario: Usuario, figurita: Figurita) {
        if (condicionAccionAdicional(usuario)){
            mailSender.sendMail(crearMail(usuario, figurita))
        }
    }

    fun crearMail(usuario: Usuario, figurita: Figurita) = Mail(
        origin = "info@worldcapp.com.ar",
        to = usuario.email,
        title = "¡Felicitaciones!",
        body =  """"Hola ${usuario.nombre}.
                    Te felicitamos por haber completado el album con la siguiente figurita:
                    ${figurita.emitirDescripcion()}"""
    )

    fun condicionAccionAdicional(usuario: Usuario): Boolean = usuario.albumLleno()

}
