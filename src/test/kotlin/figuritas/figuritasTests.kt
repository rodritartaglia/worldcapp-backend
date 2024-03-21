package figuritas

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.ints.shouldBePositive
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate


class FiguritaSpec : DescribeSpec({

    describe("Validacion de figuritas"){
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
            seleccion = seleccionValida
        )//Tuve que cambiar nombre, apellido y seleccion a var
        it("Para ser valida tiene que tener un numero positivo y su jugador debe ser valido"){
            val figuritaValida = Figurita().apply {
                numero =  129
                jugador = jugadorValido
            }//Tuve que cambiar numero y jugador a var
            figuritaValida.esValido() shouldBe true
        }
        it("Invalida por falta/error de datos"){
            val figuritaInvalidaDatosIncorrectos = Figurita().apply {
                numero = -1
                jugador = jugadorValido
            }
            shouldThrow<InvalidEntityException> { figuritaInvalidaDatosIncorrectos.validarDatos() }
        }
        it("Invalida por jugador invalido"){
            val figuritaInvalidaPorJugadorInvalido = Figurita().apply {
                numero =  129
                jugador = jugadorValido.apply { peso = -70.0 }
            }
            shouldThrow<InvalidEntityException> { figuritaInvalidaPorJugadorInvalido.validarDatos() }
        }
    }

    describe("Figuritas del mundial"){
        describe("On fire"){
            describe("De número par"){
                it("De impresion alta, calcula el valor base"){
                    val figuritaParOnFireNormal = Figurita(
                        numero = 10,
                        onFire = true,
                        nivelDeImpresion = NivelImpresion.ALTA
                    )
                    figuritaParOnFireNormal.valorBase().shouldBe(112.2 plusOrMinus (0.001))
                }
                it("De impresion baja, calcula el valor base"){
                    val figuritaParOnFireRara = Figurita(
                        numero = 2,
                        onFire = true,
                        nivelDeImpresion = NivelImpresion.BAJA
                    )
                    figuritaParOnFireRara.valorBase().shouldBe(132.0 plusOrMinus (0.001))
                }
            }
            describe("Si es impar"){
                it("De impresion alta, calcula el valor base"){
                    val figuritaImparOnFireNormal = Figurita(
                        numero = 3,
                        onFire = true,
                        nivelDeImpresion = NivelImpresion.ALTA
                    )
                    figuritaImparOnFireNormal.valorBase().shouldBe(102.0 plusOrMinus (0.001))
                }
                it("De impresion baja, calcula el valor base"){
                    val figuritaImparOnFireRara = Figurita(
                        numero = 3,
                        onFire = true,
                        nivelDeImpresion = NivelImpresion.BAJA
                    )
                    figuritaImparOnFireRara.valorBase().shouldBe(120.0 plusOrMinus (0.001))
                }
            }
        }
        describe("Si no esta on fire"){
            describe("De número par"){
                it("De impresion alta, calcula el valor base"){
                    val figuritaParNormal = Figurita(
                        numero = 10,
                        onFire = false,
                        nivelDeImpresion = NivelImpresion.ALTA
                    )
                    figuritaParNormal.valorBase().shouldBe(93.5 plusOrMinus(0.001))
                }
                it("De impresion baja, calcula el valor base"){
                    val figuritaParRara = Figurita(
                        numero = 2,
                        onFire = false,
                        nivelDeImpresion = NivelImpresion.BAJA
                    )
                    figuritaParRara.valorBase().shouldBe(110.0 plusOrMinus (0.001))
                }
            }
            describe("Si es impar"){
                it("De impresion alta, calcula el valor base"){
                    val figuritaImparNormal = Figurita(
                        numero = 3,
                        onFire = false,
                        nivelDeImpresion = NivelImpresion.ALTA
                    )
                    figuritaImparNormal.valorBase().shouldBe(85.0 plusOrMinus (0.001))
                }
                it("De impresion baja, calcula el valor base"){
                    val figuritaImparRara = Figurita(
                        numero = 3,
                        onFire = false,
                        nivelDeImpresion = NivelImpresion.BAJA
                    )
                    figuritaImparRara.valorBase().shouldBe(100.0 plusOrMinus (0.001))
                }
            }
        }
    }
})