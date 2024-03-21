package figuritas

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.shouldBePositive
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.ints.shouldBePositive
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class JugadorTest: DescribeSpec({
    describe("Validacion Jugador"){
        val seleccionValida = Seleccion(
            pais = "Pais",
            cantidadCopasMundiales = 3,
            cantidadCopasConfederacion = 2
        )
        it("para que sea valido los strings no tienen que ser vacios, los numeros mayores a 0, el numero de camiseta entre 1 y 99 y debe tener una seleccion valida"){
            val jugadorValido = Jugador(
                nombre = "nombre",
                apellido = "apellido",
                nroCamiseta = 9,
                seleccion = seleccionValida,
                altura = 1.82,
                peso = 79.4,
                cotizacion = 20000.0,
            )
            jugadorValido.esValido() shouldBe true
        }
        it("jugador invalido por falta/error de datos"){
            val jugadorInvalido = Jugador(
                nombre = "nombre",
                apellido = "",
                nroCamiseta = 9,
                seleccion = seleccionValida,
                altura = 1.82,
                peso = 79.4,
                cotizacion = 20000.0
            )
            shouldThrow<InvalidEntityException> { jugadorInvalido.validarDatos() }
        }
        it("jugador invalido por seleccion invalida"){
            val jugadorInvalido = Jugador(
                    nombre = "nombre",
                    apellido = "apellido",
                    nroCamiseta = 9,
                    seleccion = seleccionValida.apply { cantidadCopasConfederacion = -1 },
                    altura = 1.82,
                    peso = 79.4,
                    cotizacion = 20000.0
            )
            shouldThrow<InvalidEntityException> { jugadorInvalido.validarDatos() }
        }
    }
    describe("Comprobamos los metodos de un jugador"){
        val jugadorTesteado = Jugador(
            nombre = "Lionel",
            apellido = "Messi",
            fechaDeNacimiento = LocalDate.now().minusYears(22),
            nroCamiseta = 10,
            esLider = true,
            seleccion = Seleccion(
                pais = "Argentina",
                confederacion = Confederacion.CONMEBOL,
                cantidadCopasMundiales = 3,
                cantidadCopasConfederacion = 2
            ),
            anioDebut = LocalDate.of(2005,8,17),
            altura = 1.7,
            peso = 72.0,
            cotizacion = 50.0,
            posicion = Delantero
        )
        it("comprobamos la edad del jugador"){
            val edadAcordeDelJugador = 22
            jugadorTesteado.edad().shouldBe(edadAcordeDelJugador)
        }
        
        it("comprobamos que se toman cierta cantidad de caracteres del pais"){
            val cantidadDeCaracteresQueToma = 3
            jugadorTesteado.pais().count().shouldBe(cantidadDeCaracteresQueToma)
        }
        it("El jugador es campeon del mundo"){
            jugadorTesteado.esCampeonDelMundo().shouldBe(true)
        }
        it("El jugador no es alto"){
            jugadorTesteado.esAlto().shouldBe(false)
        }
        it("El jugador no es ligero"){
            jugadorTesteado.esLigero().shouldBe(false)
        }
        it("El jugador tiene muchos anios en seleccion"){
            jugadorTesteado.muchosAniosEnSeleccion().shouldBe(true)
        }
        it("El jugador tiene copas ganadas"){
            jugadorTesteado.copasGanadas().shouldNotBe(0)
        }
        it("El jugador no tiene una cantidad par de copas ganadas"){
            jugadorTesteado.seleccionTieneCopasPar().shouldBe(false)
        }
        it("El jugador es leyenda"){
            jugadorTesteado.esLeyenda().shouldBe(true)
        }
        it("El jugador no es promesa"){
            jugadorTesteado.esPromesa().shouldBe(false)
        }
    }
    describe("Comprobamos la valoracion de cada tipo de jugador"){

        describe("valoracion del jugador ARQUERO"){
            it("ARQUERO condicion de posicion verdadera"){
                val arquero = Jugador(posicion = Arquero, altura = 1.8)
                val valoracionConBonus = 180.0
                arquero.valoracion().shouldBe(valoracionConBonus)
            }

            it("ARQUERO condicion de posicion falsa"){
                val arquero = Jugador(posicion = Arquero)
                val valorPisoDelJugador = 100.0
                arquero.valoracion().shouldBe(valorPisoDelJugador)
            }
        }
        describe("valoracion del jugador DEFENSOR"){
            it("DEFENSOR condicion de posicion verdadera"){
                val defensor = Jugador(
                    posicion = Defensor,
                    anioDebut = LocalDate.now().minusYears(10))
                val valoracionConBonus = 150.0
                defensor.valoracion().shouldBe(valoracionConBonus)
            }
            it("DEFENSOR condicion de posicion falsa"){
                val defensor = Jugador(posicion = Defensor, esLider = false)
                val valorPisoDelJugador = 50.0
                defensor.valoracion().shouldBe(valorPisoDelJugador)
            }
        }

        describe("valoracion del jugador MEDIOCAMPISTA"){
            it("MEDIOCAMPISTA condicion de jugador verdadera"){
                val mediocampista = Jugador(posicion = Mediocampista)
                val valoracionConBonus = 215.0
                mediocampista.valoracion().shouldBe(valoracionConBonus)
            }
            it("MEDIOCAMPISTA condicion de jugador falsa"){
                val mediocampista = Jugador(posicion = Mediocampista, peso = 80.0)
                val valorPisoDelJugador = 150.0
                mediocampista.valoracion().shouldBe(valorPisoDelJugador)
            }
        }

        describe("valoracion del jugador DELTANTERO"){
            it("DELTANTERO condicion de jugador verdadera"){
                val seleccionCampeonMundial = Seleccion(cantidadCopasMundiales = 1)
                val delantero = Jugador(posicion = Delantero, seleccion = seleccionCampeonMundial)
                val valoracionConBonus = 210
                delantero.valoracion().shouldBe(valoracionConBonus)
            }

            it("DELTANTERO condicion de jugador falsa"){
                val delantero = Jugador(posicion = Delantero)
                val valorPisoDelJugador = 200.0
                delantero.valoracion().shouldBe(valorPisoDelJugador)
            }
        }

        describe("valoracion del jugador POLIVALENTE"){
            it("POLIVALENTE condicion de jugador verdadera"){
                val seleccionCampeonMundial = Seleccion(cantidadCopasMundiales = 1)
                val polivalente = Jugador(
                    posicion = Polivalente(posiciones = mutableListOf(Mediocampista, Delantero)),
                    cotizacion = 10.0,
                    fechaDeNacimiento = LocalDate.now().minusYears(20),
                    anioDebut = LocalDate.now().minusYears(1),
                    seleccion = seleccionCampeonMundial)
                val valoracionConBonus = 367.5
                polivalente.valoracion().shouldBe(valoracionConBonus)
            }

            it("POLIVALENTE condicion de jugador falsa"){
                val polivalente = Jugador(
                    posicion = Polivalente(posiciones = mutableListOf(Mediocampista, Delantero)))
                val valorPisoDelJugador = 175.0
                polivalente.valoracion().shouldBe(valorPisoDelJugador)
            }

        }

    }
})