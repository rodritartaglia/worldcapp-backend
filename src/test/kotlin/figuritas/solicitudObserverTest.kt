package figuritas

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify

class solicitudObserverTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Observers") {
        val figurita = Figurita(
            numero = 10,
            jugador = Jugador(
                nombre = "Lionel",
                apellido = "Messi",
                seleccion = Seleccion(
                    pais = "Argentina"
                )
            )
        )
        val figuritaValiosa = Figurita(2 ,true)
        val figuritaPocoValiosa = Figurita()
        val triplicarKmObserver = TriplicarKmObserver()
        val cambioANacionalistaObserver = cambioANacionalistaObserver()
        val repetidaMenosValiosaobserver = RepetidaReservadaMenosValiosaObserver(figuritasRepetidasReservadas = mutableListOf(figurita, figuritaValiosa))
        val cambioADesprendidoObserver = CambioADesprendidoObserver(cantidadDeFiguritasARegalar = 3)
        val proveedor = Usuario(figuritasRepetidas = mutableListOf(figurita))
        val solicitante = Usuario(
            figuritasFaltantes = mutableListOf(figurita),
            observersSolicitud = mutableListOf(
                triplicarKmObserver,
                cambioANacionalistaObserver,
                repetidaMenosValiosaobserver,
                cambioADesprendidoObserver)
        )

        it("TriplicarKmObserver triplica la distancia relativa") {
            solicitante.cambiarFigurita(proveedor, figurita)

            solicitante.cercaniaRelativa shouldBe 150000.0
            triplicarKmObserver.shouldNotBeIn(solicitante.observersSolicitud)
        }

        it("cambioANacionalistaObserver cambia el tipo de usuario a Nacionalista") {
            solicitante.agregarFiguritaFaltante(figurita)
            solicitante.agregarFiguritaFaltante(figurita)
            solicitante.agregarFiguritaFaltante(figurita)
            solicitante.agregarFiguritaFaltante(figurita)
            proveedor.agregarFiguritaRepetida(figurita)
            proveedor.agregarFiguritaRepetida(figurita)
            proveedor.agregarFiguritaRepetida(figurita)

            solicitante.cambiarFigurita(proveedor, figurita)
            solicitante.cambiarFigurita(proveedor, figurita)
            solicitante.cambiarFigurita(proveedor, figurita)

            solicitante.tipoDeUsuario.javaClass shouldBe Nacionalista().javaClass
        }

        it("RepetidaReservadaMenosValiosaObserver"){
            repetidaMenosValiosaobserver.agregarFiguritaReservada(figuritaValiosa)
            repetidaMenosValiosaobserver.agregarFiguritaReservada(figuritaPocoValiosa)

            solicitante.cambiarFigurita(proveedor, figurita)

            figuritaPocoValiosa.shouldNotBeIn(repetidaMenosValiosaobserver.figuritasRepetidasReservadas)
            figuritaPocoValiosa shouldBeIn solicitante.figuritasRepetidas
        }

        it("cambioADesprendidoObserver"){
            solicitante.agregarFiguritaRepetida(figurita)
            solicitante.agregarFiguritaRepetida(figurita)
            solicitante.agregarFiguritaRepetida(figurita)
            solicitante.agregarFiguritaRepetida(figurita)


            solicitante.cambiarFigurita(proveedor, figurita)

            solicitante.tipoDeUsuario shouldBe Desprendido

        }

        it("NotificacionObserver"){
            val mockedmailSender = mockedMailSender()
            val notificacionObserver = NotificacionObserver(mockedmailSender)
            solicitante.aniadirAccion(notificacionObserver)

            solicitante.cambiarFigurita(proveedor, figurita)

            verify(exactly = 1){
                mockedmailSender.sendMail(
                    mail = Mail(
                        origin = "info@worldcapp.com.ar",
                        to = solicitante.email,
                        title = "¡Felicitaciones!",
                        body =  """"Hola ${solicitante.nombre}.
                    Te felicitamos por haber completado el album con la siguiente figurita:
                    ${figurita.emitirDescripcion()}"""
                    )
                )
            }

        }
    }
}
)

fun mockedMailSender(): MailSender = mockk<MailSender>(relaxUnitFun = true)