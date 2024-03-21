package figuritas

import ar.edu.unsam.algo3.repository.Actualizador
import ar.edu.unsam.algo3.repository.Repositorio
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.mockk.verify
import org.uqbar.geodds.Point
import java.time.LocalDate

class ProcesosDeAdminTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val mockedmailSender = mockedMailSender()

    describe("Borrar usuario inactivo") {
        val repositorio = Repositorio<Usuario>()
        val usuarioActivo = Usuario(figuritasFaltantes = mutableListOf(Figurita()))
        val usuarioInactivo = Usuario()
        val proceso = BorrarUsuarioInactivo(repositorio).apply{
            mailSender = mockedmailSender
        }
        repositorio.create(usuarioActivo)
        repositorio.create(usuarioInactivo)

        proceso.iniciarEjecucion()

        it("Corroboro la eliminacion del usuario inactivo"){
            usuarioInactivo shouldNotBeIn repositorio.objetos
        }
        it("Corroboro el envio de mail"){
            verifyMock(proceso)
        }
    }

    describe("Creacion o actualizacion de selecciones") {
        val repositorio = Repositorio<Seleccion>()
        val seleccion = Seleccion(pais = "Argentina")
        val json = """
        [
            {
            "id": 1,
            "pais": "Argentina",
            "confederacion": "CONMEBOL",
            "copasDelMundo": 3,
            "copasConfederacion": 15
            },
            {
            "pais": "Brasil",
            "confederacion": "CONMEBOL",
            "copasDelMundo": 5,
            "copasConfederacion": 9
            }
        ]
        """
        val actualizadorTest = Actualizador().apply { repoAActualizar = repositorio
                                                  service = StubService(json)}
        val proceso = CreacionOActualizacionDeSelecciones.apply{
            actualizador = actualizadorTest
            mailSender = mockedmailSender
        }

        repositorio.create(seleccion)
        proceso.iniciarEjecucion()

        it("Corroboro que se actualizo el repositorio"){
            repositorio.getById(1).cantidadCopasConfederacion shouldBe 15
            repositorio.getById(2).pais shouldBe "Brasil"
            repositorio.getById(2).cantidadCopasMundiales shouldBe 5
        }
        it("Corroboro que se envio el mail"){
            verifyMock(proceso)
        }

    }

    describe("Cambiar estado on fire") {
        val repositorio = Repositorio<Figurita>()
        val proceso = CambiarEstadoOnFire(repositorio).apply{
            mailSender = mockedmailSender
        }

        val seleccionValida = Seleccion(
            pais = "Pais",
            cantidadCopasMundiales = 1,
            cantidadCopasConfederacion = 2)

        val jugadorValido = Jugador(
            nombre = "nombre",
            apellido = "apellido",
            nroCamiseta = 10,
            altura = 1.7,
            peso = 70.0,
            cotizacion = 10000.0,
            seleccion = seleccionValida)

        val figuritaNotOnFire = Figurita(numero = 10, jugador = jugadorValido, onFire = false)
        repositorio.create(figuritaNotOnFire)
        proceso.iniciarEjecucion()

        it("Corroboro el estado de la figurita ahora debería ser on fire"){
            figuritaNotOnFire.onFire shouldBe true
        }

        it("Corroboro que se envio el mail"){
            verifyMock(proceso)
        }

    }

    describe("Borrar punto de venta") {
        val repositorio = Repositorio<PuntoDeVenta>()
        val puntoDeVentaInactivo = Kiosco().apply {
            nombre = "kiosco"
            direccion = Direccion("provincia","localidad","calle",1111, Point(784.0,-424.36))
            stockActual = 0
        }
        val proceso = BorarPuntoDeVenta(repositorio).apply{
            mailSender = mockedmailSender
        }
        repositorio.create(puntoDeVentaInactivo)
        proceso.iniciarEjecucion()

        it("Corroboro la eliminacion del punto de venta"){
            repositorio.objetos.shouldBeEmpty()
        }
        it("Corroboro que se envio el mail"){
            verifyMock(proceso)
        }
    }

    describe("Actualizar puntos de venta") {
        val repositorio = Repositorio<PuntoDeVenta>()
        val pedidoAFabricaPendiente = PedidoAFabrica(100, LocalDate.now().minusDays(9))
        val pedidoAFabricaEntregado = PedidoAFabrica(100, LocalDate.now().plusDays(1))
        val puntoDeVentaValido = Libreria().apply {
            nombre = "libreria"
            direccion = Direccion("provincia","localidad","calle",1111,Point(784.0,-424.36))
            stockActual = 1000
            pedidos = mutableListOf(pedidoAFabricaPendiente, pedidoAFabricaEntregado)
        }
        val proceso = ActualizarStockPuntosDeVenta(repositorio).apply {
            mailSender = mockedmailSender
        }
        repositorio.create(puntoDeVentaValido)
        proceso.iniciarEjecucion()

        it("Corroboro que se actualizaron los puntos de venta"){
            puntoDeVentaValido.stockActual shouldBe 1100
            pedidoAFabricaEntregado shouldNotBeIn puntoDeVentaValido.pedidos
            pedidoAFabricaPendiente shouldBeIn puntoDeVentaValido.pedidos
        }

        it("Corroboro que se envio el mail"){
            verifyMock(proceso)
        }
    }

})

fun verifyMock (proceso: ProcesoDeAdministrador){
    verify(exactly = 1) {
        proceso.mailSender.sendMail(
            Mail(
                origin = "sarasa",
                to = "admin@worldcapp.com.ar",
                title = "¡Felicitaciones!",
                body = "Se realizo el proceso <${proceso.tipoProceso}>"
            )
        ) }
}
