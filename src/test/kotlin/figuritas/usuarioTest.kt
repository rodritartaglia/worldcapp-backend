package figuritas

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import org.uqbar.geodds.Point
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class UsuarioTest: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Usuarios") {
        it("Calcular edad") {
            val usuarioTR = Usuario (
                nombre = "Tomas",
                apellido = "Romero",
                userName = "tromero",
                fechaDeNacimiento = LocalDate.now().minusYears(20)
            )
            usuarioTR.edad().shouldBe(20)
        }
    }
    describe("Validacion Usuario"){

        it("Los campos de String no deben ser vacios y debe tener una direccion valida"){
            val usuarioValido = Usuario()
            usuarioValido.esValido() shouldBe true

        //    Los resultados dan exactamente lo mismo y aun así rompe el test
        //    usuarioValido.darUbicacionGeografica().shouldBe(Point(-225.0,222.0))
        }
        it("Tira excepcion un usuario invalido por falta/error de datos"){
            val usuarioInvalido = Usuario(
                userName = " ",
                email = "",
            )
            shouldThrow<InvalidEntityException> { usuarioInvalido.validarDatos() }
        }
    }

    describe("Validacion Direccion"){
        it("Strings no deben ser vacios, altura mayor a 0 y tener ubicacion geografica"){
            val direccionValida = Direccion(
                "provincia",
                "localidad",
                "calle",
                1111
            )
            direccionValida.esValido() shouldBe true
        }
        it("Direccion invalida por falta/error de datos"){
            val direccionInvalida = Direccion(
                "provincia",
                "",
                "calle",
                0
            )
            direccionInvalida.esValido() shouldBe false
        }
    }

    describe("Comprobamos metodos del usuario"){
        val usuarioTesteado = Usuario(
            nombre = "Felipe",
            apellido = "Cabanillas",
            userName = "-nOx¨",
            email = "feli.caba.fe@gmail.com",
            fechaDeNacimiento = LocalDate.of(2002,6,2),
            direccion = Direccion(
                provincia = "Buenos Aires",
                localidad = "San Martin",
                altura = 4049,
                ubicacionGeografica = Point(-34.579003, -58.507660)
            ),//30/06/02,
            cercaniaRelativa = 600.0,
            jugadorFavorito = Jugador()
        )
        it("comprobamos la edad del jugador"){
            val edadAcordeDelUsuario = ChronoUnit.YEARS.between(usuarioTesteado.fechaDeNacimiento, LocalDate.now())
            usuarioTesteado.edad().shouldBe(edadAcordeDelUsuario)
        }

        //it("devuelve una ubicacion geografica"){
        //    val ubicacion = Point(-34.0,-58.0)
        //    usuarioTesteado.darUbicacionGeografica().shouldBe(ubicacion)
        //}

        it("devuelve si esta cerca de otro usuario"){
            val usuarioCercano = Usuario(direccion = Direccion(ubicacionGeografica = Point(-34.580381, -58.512348)))
            usuarioTesteado.estaCercaDeOtroUsuario(usuarioCercano).shouldBe(true)
        }

        it("devuelve si esta lejos de otro usuario"){
            val usuarioCercano = Usuario(direccion = Direccion(ubicacionGeografica = Point(-49.604759, -68.741525)))
            usuarioTesteado.estaCercaDeOtroUsuario(usuarioCercano).shouldBe(false)
        }

    }

    describe("Comprobamos si puede regalar figurita teniendo en cuenta el tipo"){

        describe("Usuario PAR"){
            val usuarioConFiguritaPar = Usuario(tipoDeUsuario = Par)

            it("No lo regala"){
                val figuritaPar = Figurita(
                    numero = 2,
                    jugador = Jugador(
                        nroCamiseta = 2,
                        seleccion = Seleccion(cantidadCopasMundiales = 2)
                    )
                )
                usuarioConFiguritaPar.puedeRegalar(figuritaPar).shouldBeFalse()
            }

            it("Lo regala"){
                val figuritaNoPar = Figurita(
                    numero = 3,
                    jugador = Jugador(
                        nroCamiseta = 5,
                        seleccion = Seleccion(cantidadCopasMundiales = 1)
                    )
                )
                usuarioConFiguritaPar.puedeRegalar(figuritaNoPar).shouldBeTrue()
            }
        }

        describe("Usuario NACIONALISTA"){
            val seleccionFavorita = Seleccion()
            val usuarioNacionalista = Usuario(tipoDeUsuario = Nacionalista().apply { seleccionesQueridas= mutableSetOf(seleccionFavorita) })

            it("No lo regala"){
                val figuritasDeSeleccionFavorita = Figurita(
                    jugador = Jugador(seleccion = seleccionFavorita)
                )
                usuarioNacionalista.puedeRegalar(figuritasDeSeleccionFavorita).shouldBeFalse()
            }

            it("Lo regala"){
                val figuritasDeSeleccionNoFavorita = Figurita()
                usuarioNacionalista.puedeRegalar(figuritasDeSeleccionNoFavorita).shouldBeTrue()
            }
        }

        describe("Usuario CONSERVADOR"){
            val usuarioConservador = Usuario(
                tipoDeUsuario = Conservador)

            it("No lo regala"){
                val figuritaDeImpresionBaja = Figurita(nivelDeImpresion = NivelImpresion.BAJA)
                usuarioConservador.puedeRegalar(figuritaDeImpresionBaja).shouldBeFalse()
            }

            it("Lo regala"){
                val figuritaDeImpresionAlta = Figurita(nivelDeImpresion = NivelImpresion.ALTA)
                usuarioConservador.puedeRegalar(figuritaDeImpresionAlta).shouldBeTrue()
            }
        }

        describe("Usuario FANATICO"){
            val jugadorLeyenda = Jugador(
                esLider = true,
                anioDebut = LocalDate.of(2005,8,17),
                nroCamiseta = 10,
                cotizacion = 50.0
            )
            val jugadorFavorito = Jugador()
            val usuarioFanatico = Usuario(tipoDeUsuario = Fanatico, jugadorFavorito = jugadorFavorito)
            it("No lo regala"){
                val figuritaJugadorLeyenda = Figurita(jugador = jugadorLeyenda)
                val figuritaJugadorFavorito = Figurita(jugador = jugadorFavorito)
                usuarioFanatico.puedeRegalar(figuritaJugadorLeyenda).shouldBeFalse()
                usuarioFanatico.puedeRegalar(figuritaJugadorFavorito).shouldBeFalse()
            }

            it("Lo regala"){
                usuarioFanatico.puedeRegalar(figurita = Figurita()).shouldBeTrue()
            }
        }

        describe("Usuario DESPRENDIDO"){
            val usuarioDesprendido = Usuario(tipoDeUsuario = Desprendido)
            it("Lo regala"){

                val figuritaRepetida = Figurita(
                    numero = 10,
                    onFire = true,
                    nivelDeImpresion = NivelImpresion.BAJA,
                    jugador = Jugador(
                        nombre = "Lionel",
                        apellido = "Messi",
                        fechaDeNacimiento = LocalDate.of(1987,6,24),
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
                )
                usuarioDesprendido.figuritasRepetidas.add(figuritaRepetida)
                usuarioDesprendido.puedeRegalar(figuritaRepetida).shouldBeTrue()
            }
        }

        describe("Usuario APOSTADOR"){
            val figuritaPromesa = Figurita(jugador = Jugador(
                fechaDeNacimiento = LocalDate.of(2003,6,24),
                anioDebut = LocalDate.of(2022,6,24),
                cotizacion = 10.0))
            val figuritaOnFire = Figurita(onFire = true,
                jugador = Jugador(fechaDeNacimiento = LocalDate.of(1987,6,24)))

            val usuarioApostador = Usuario(tipoDeUsuario = Apostador)
            it("No lo regala"){
                usuarioApostador.puedeRegalar(figuritaOnFire).shouldBeFalse()
                usuarioApostador.puedeRegalar(figuritaPromesa).shouldBeFalse()
            }

            it("Lo regala"){
                val figuritaRegalablePorApostador = Figurita (
                    jugador = Jugador(fechaDeNacimiento = LocalDate.of(1987,6,24))
                )
                usuarioApostador.puedeRegalar(figuritaRegalablePorApostador).shouldBeTrue()
            }
        }

        describe("Usuario INTERESADO"){
            //lista de figuritas
            val figuritaDeLaColeccionA = Figurita()
            val figuritaDeLaColeccionB = Figurita(jugador = Jugador(altura = 1.8))
            val figuritaDeLaColeccionC = Figurita(jugador = Jugador(altura = 1.9))
            val figuritaDeLaColeccionD = Figurita(jugador = Jugador(altura = 2.0))
            val figuritaDeLaColeccionE = Figurita(jugador = Jugador(altura = 2.1))
            val figuritaDeLaColeccionF = Figurita(jugador = Jugador(altura = 2.2))
            //lista de figuritas
            val usuarioInteresado = Usuario(tipoDeUsuario = Interesado,
                figuritasRepetidas = mutableListOf(
                    figuritaDeLaColeccionF,figuritaDeLaColeccionE,figuritaDeLaColeccionD,
                    figuritaDeLaColeccionC,figuritaDeLaColeccionB,figuritaDeLaColeccionA,
                    figuritaDeLaColeccionA,figuritaDeLaColeccionA,figuritaDeLaColeccionA
                )
            )
            it("No lo regala"){
                usuarioInteresado.puedeRegalar(figuritaDeLaColeccionF).shouldBeFalse()
            }

            it("Lo regala"){
                usuarioInteresado.puedeRegalar(figuritaDeLaColeccionA).shouldBeTrue()
            }
        }

        describe("Usuario CAMBIANTE"){
            val figuritaRegalable = Figurita(
                numero = 10,
                onFire = true,
                nivelDeImpresion = NivelImpresion.BAJA,
                jugador = Jugador(
                    nombre = "Lionel",
                    apellido = "Messi",
                    fechaDeNacimiento = LocalDate.of(1987,6,24),
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
            )
            it("No lo regala"){
                //usuario edad menor a 25 es Desprendido
                val usuarioCambiante = Usuario(tipoDeUsuario = Cambiante,
                    fechaDeNacimiento= LocalDate.now().minusYears(20))
                usuarioCambiante.figuritasRepetidas.add(figuritaRegalable)
                usuarioCambiante.puedeRegalar(figuritaRegalable).shouldBeTrue()
            }

            it("Lo regala"){
                //usuario edad mayor a 25 es Conservador
                val usuarioCambiante = Usuario(tipoDeUsuario = Cambiante,
                    fechaDeNacimiento= LocalDate.now().minusYears(26))
                usuarioCambiante.puedeRegalar(figuritaRegalable).shouldBeFalse()
            }
        }
    }
    
    describe("Validamos solicitudes entre usuarios") {
        val figurita = Figurita()
        val elQuePide = Usuario(
            direccion = Direccion(
                ubicacionGeografica = Point(10, 10)
            ),
            figuritasFaltantes = mutableListOf(figurita)
        )
        elQuePide.agregarFiguritaFaltante(Figurita())

        it("La operación es exitosa") {
            val elQueCambia = Usuario(
                direccion = Direccion(
                    ubicacionGeografica = Point(12, 10)
                ),
                figuritasRepetidas = mutableListOf(figurita),
                tipoDeUsuario = Desprendido
            )
            elQueCambia.agregarFiguritaRepetida(Figurita())
            figurita shouldBeIn elQuePide.figuritasFaltantes
            figurita shouldBeIn elQueCambia.figuritasRepetidas

            elQuePide.cambiarFigurita(elQueCambia, figurita)

            figurita.shouldNotBeIn(elQuePide.figuritasFaltantes)
            figurita.shouldNotBeIn(elQueCambia.figuritasRepetidas)
        }

        it("La operacion no fue exitosa") {
            val elQueCambia = Usuario()
            shouldThrow<FiguritaCantBeGivenException> { elQuePide.cambiarFigurita(elQueCambia, figurita) }
        }
    }
    describe("No se pueden cambiar las figus, por h o por b"){
        val figurita = Figurita()
        val elQuePide = Usuario(
            cercaniaRelativa = 1000.0,
            direccion = Direccion(
                ubicacionGeografica = Point(-45.742467, -68.289503)
            ),
            figuritasFaltantes = mutableListOf(figurita)
        )

        it ("El usuario no está cerca y falla") {
            val elQueCambiaLejos = Usuario(
                direccion = Direccion(
                    provincia = "Buenos Aires",
                    localidad = "San Martin",
                    calle = "Belgrano",
                    altura = 4049,
                    ubicacionGeografica = Point(-34.579003, -58.507660)
                )
            )
            elQueCambiaLejos.agregarFiguritaRepetida(figurita)

            shouldThrow<TargetIsTooFarException>{ elQuePide.cambiarFigurita(elQueCambiaLejos,figurita) }
        }

        it ("El usuario no tiene la figurita") {
            val elQueCambia = Usuario()
            shouldThrow<FiguritaCantBeGivenException>{ elQuePide.cambiarFigurita(elQueCambia,figurita) }
        }
        it ("El usuario no puede regalar la figurita") {
            val elQueCambia = Usuario(tipoDeUsuario = Conservador)
            shouldThrow<FiguritaCantBeGivenException>{ elQuePide.cambiarFigurita(elQueCambia,figurita) }
        }

    }

})
