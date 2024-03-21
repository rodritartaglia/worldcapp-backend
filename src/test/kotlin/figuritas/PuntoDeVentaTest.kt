package figuritas

import io.kotest.assertions.print.print
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

import org.uqbar.geodds.Point
import java.lang.Exception
import java.time.DayOfWeek
import java.time.LocalDate

class PuntoDeVentaTest: DescribeSpec({


    describe("Probamos validez de puntos de venta") {
        it("Para que sea valido sus strings no deben ser vacios, el stock mayor o igual a 0 y debe tener una direccion valida"){
            val puntoDeVentaValido = Kiosco().apply {
                nombre = "kiosco"
                direccion = Direccion("provincia","localidad","calle",1111,Point(784.0,-424.36))
                stockActual = 1000
            }
            puntoDeVentaValido.esValido() shouldBe true
        }
        it("Punto de venta invalido"){
            val puntoDeVentaInvalido = Kiosco().apply {
                nombre = ""
                direccion = Direccion("provincia","localidad","calle",1111,Point(784.0,-424.36))
                stockActual = 1000
            }
            shouldThrow<InvalidEntityException>{puntoDeVentaInvalido.validarDatos()}
        }
    }

    describe("Comprobamos metodos de la clase abstracta punto de venta"){
        val puntoDeVentaGenerico = Kiosco()

        it("No tiene disponibilidad"){
            puntoDeVentaGenerico.disponibilidad() shouldBe false
        }

        it("No hay pedidos a fabrica pendientes"){
            puntoDeVentaGenerico.pedidosPendientes() shouldBe emptyList()
        }

    }

    describe("Probamos el importe total del Kiosco"){
        describe("Atendido por el jefe"){
            val kioscoAtendidoPorJefe = Kiosco()

            it("El cliente esta cerca del establecimiento"){
                val clienteCercano = Usuario()

                kioscoAtendidoPorJefe.importeTotal(clienteCercano,10) shouldBe 2970.0
            }

            it("El cliente esta lejos del establecimiento"){
                val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                kioscoAtendidoPorJefe.importeTotal(clienteLejano,10) shouldBe 3190.0
            }

        }

        describe("Atendido por empleados"){
            val kioscoAtendidoPorEmpleado = Kiosco().apply { esAtendidoPorEmpleado = true }

            it("El cliente esta cerca del establecimiento"){
                val clienteCercano = Usuario()

                kioscoAtendidoPorEmpleado.importeTotal(clienteCercano,10) shouldBe 3375.0
            }

            it("El cliente esta lejos del establecimiento"){
                val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                kioscoAtendidoPorEmpleado.importeTotal(clienteLejano,10) shouldBe 3625.0
            }
        }
    }

    describe("Probamos el importe total de la libreria"){

        describe("Libreria sin pedidos pendientes"){
            val libreriaSinPedidosPendientes = Libreria()

            it("El cliente esta cerca del establecimiento"){
                val clienteCercano = Usuario()

                libreriaSinPedidosPendientes.importeTotal(clienteCercano,10) shouldBe 2970.0
            }

            it("El cliente esta lejos del establecimiento"){
                val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                libreriaSinPedidosPendientes.importeTotal(clienteLejano,10) shouldBe 3190.0
            }

        }

        describe("Atendido por empleados"){
            val libreriaConPedidosPendientes = Libreria().apply { pedidos.add(PedidoAFabrica()) }
            it("El cliente esta cerca del establecimiento"){
                val clienteCercano = Usuario()

                libreriaConPedidosPendientes.importeTotal(clienteCercano,10) shouldBe 2835.0
            }

            it("El cliente esta lejos del establecimiento"){
                val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                libreriaConPedidosPendientes.importeTotal(clienteLejano,10) shouldBe 3045.0
            }
        }
    }

    describe("Probamos el importe del Supermercado"){

        describe("Supermercado sin descuento"){
            val supermercadoSinDescuento = Supermercado().apply { SinDescuento }

            it("El cliente esta cerca del establecimiento"){
                val clienteCercano = Usuario()

                supermercadoSinDescuento.importeTotal(clienteCercano,10) shouldBe 2700.0
            }

            it("El cliente esta lejos del establecimiento"){
                val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                supermercadoSinDescuento.importeTotal(clienteLejano,10) shouldBe 2900.0
            }

        }

        describe("Supermercado con descuento los jueves") {
            val supermercadoConDescuento = Supermercado().apply { tipoDeDescuento = mockDescuentoDiaParticular(DayOfWeek.THURSDAY) }

            describe("Hay descuento") {

                it("El cliente esta cerca del establecimiento") {
                    val clienteCercano = Usuario()
                    supermercadoConDescuento.importeTotal(clienteCercano, 10) shouldBe 2430.0
                }

                it("El cliente esta lejos del establecimiento") {
                    val clienteLejano = Usuario(
                        direccion = Direccion(
                            ubicacionGeografica = Point(
                                0.0008839662394774515,
                                -0.10339459855389413
                            )
                        )
                    ) //2 km de más aprox

                    supermercadoConDescuento.importeTotal(clienteLejano, 10) shouldBe 2610.0
                }
            }

            describe("No hay descuento"){
                val supermercadoConDescuento = Supermercado().apply { tipoDeDescuento = mockDescuentoDiaParticular(DayOfWeek.MONDAY) }

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoConDescuento.importeTotal(clienteCercano,10) shouldBe 2700.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoConDescuento.importeTotal(clienteLejano,10) shouldBe 2900.0
                }
            }
}

        describe("Supermercado con descuento los primeros diez dias"){

            describe("Hay descuento"){
                val supermercadoConDescuento = Supermercado().apply { tipoDeDescuento = mockDescuentoPrimerosDiasDelMes(5) }

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoConDescuento.importeTotal(clienteCercano,10) shouldBe 2565.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoConDescuento.importeTotal(clienteLejano,10) shouldBe 2755.0
                }
            }

            describe("No hay descuento"){
                val supermercadoConDescuento = Supermercado().apply { tipoDeDescuento = mockDescuentoPrimerosDiasDelMes(20) }

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoConDescuento.importeTotal(clienteCercano,10) shouldBe 2700.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoConDescuento.importeTotal(clienteLejano,10) shouldBe 2900.0
                }
            }

        }

        describe("Supermercado con descuento por compra por mayor"){
            val supermercadoConDescuento = Supermercado().apply { tipoDeDescuento = DescuentoCompraAlMayor }

            describe("Hay descuento"){
                val compraConDescuento = 300

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoConDescuento.importeACobrar(compraConDescuento,clienteCercano.direccion) shouldBe 52000.0
                    supermercadoConDescuento.importeTotal(clienteCercano,compraConDescuento) shouldBe 28600.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoConDescuento.importeACobrar(compraConDescuento,clienteLejano.direccion) shouldBe 52200.0
                    supermercadoConDescuento.importeTotal(clienteLejano,compraConDescuento) shouldBe 28710.0
                }
            }

            describe("No hay descuento"){
                val compraSinDescuento = 100

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoConDescuento.importeTotal(clienteCercano,compraSinDescuento) shouldBe 18000.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoConDescuento.importeTotal(clienteLejano,compraSinDescuento) shouldBe 18200.0
                }
            }

        }

        describe("Supermercado con multiples descuentos"){

            describe("Descuento los jueves y los primeros dias del mes"){
                val supermercadoDosDescuentos = Supermercado().apply {
                    tipoDeDescuento = DescuentosAcumulados(descuentos = mutableSetOf<Descuento>(
                        mockDescuentoDiaParticular(DayOfWeek.THURSDAY),
                        mockDescuentoPrimerosDiasDelMes(5)))
                }
                val diaConDescuento = LocalDate.of(2023,4,6)

                it("Porcentaje de descuento"){
                    val limiteDelDescuento: Double = 1.0
                    supermercadoDosDescuentos.porcentajeDelPuntoDeVenta(10) shouldBe  0.85
                }

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoDosDescuentos.importeTotal(clienteCercano,10) shouldBe 2295.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoDosDescuentos.importeTotal(clienteLejano,10) shouldBe 2465.0
                }

            }

            describe("Descuento los jueves, los primeros dias del mes y por compra al por mayor"){
                val supermercadoTresDescuento = Supermercado().apply {
                    tipoDeDescuento = DescuentosAcumulados(descuentos = mutableSetOf<Descuento>(
                        mockDescuentoDiaParticular(DayOfWeek.THURSDAY),
                        mockDescuentoPrimerosDiasDelMes(5),
                        DescuentoCompraAlMayor)
                    )
                }
                val cantidadFiguritasParaDescuento = 300
                val diaConDescuento = LocalDate.of(2023,4,6)
                it("El descuento al limite"){
                    val limiteDelDescuento: Double = 0.5
                    supermercadoTresDescuento.porcentajeDelPuntoDeVenta(cantidadFiguritasParaDescuento) shouldBe limiteDelDescuento
                }

                it("El cliente esta cerca del establecimiento"){
                    val clienteCercano = Usuario()

                    supermercadoTresDescuento.importeTotal(clienteCercano,cantidadFiguritasParaDescuento) shouldBe 26000.0
                }

                it("El cliente esta lejos del establecimiento"){
                    val clienteLejano = Usuario(direccion = Direccion(ubicacionGeografica = Point(0.0008839662394774515, -0.10339459855389413))) //2 km de más aprox

                    supermercadoTresDescuento.importeTotal(clienteLejano,cantidadFiguritasParaDescuento) shouldBe 26100.0
                }

            }

        }


    }

})

fun mockDescuentoPrimerosDiasDelMes(diaDelMesATestear: Int): Descuento{
    val descuentoMock = spyk<DescuentoPrimerosDiasDelMes>()
    every { descuentoMock.diaDelMes() } returns diaDelMesATestear
    return descuentoMock
}

fun mockDescuentoDiaParticular(diaATestear: DayOfWeek): Descuento {
    val descuentoMock = spyk<DescuentoDiaDeLaSemana>()
    every { descuentoMock.condicionDeDescuentoPorDia() } answers { diaATestear == descuentoMock.diaDelDescuento() }
    return descuentoMock
}