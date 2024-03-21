package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.repository.*
import figuritas.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import org.uqbar.geodds.Point
import java.time.LocalDate

@Component
class WorldCAppBootstrap(
    val repositorioUsuarios: RepositorioUsuarios,
    val repositorioFiguritas: RepositorioFiguritas,
    val repositorioPuntoDeVenta: RepositorioPuntoDeVenta,
    val repositorioJugadores: RepositorioJugadores,
    val repositorioSelecciones: RepositorioSelecciones
) : InitializingBean
{
    override fun afterPropertiesSet() {

        arrayOf(
            Seleccion(pais = "Argentina", confederacion = Confederacion.CONMEBOL),
            Seleccion(pais = "Portugal", confederacion = Confederacion.UEFA),
            Seleccion(pais = "España", confederacion = Confederacion.UEFA),
            Seleccion(pais = "Suecia", confederacion = Confederacion.UEFA),
            Seleccion(pais = "Francia", confederacion = Confederacion.UEFA)
        ).forEach { repositorioSelecciones.create(it) }

        arrayOf(
            Jugador( "Lionel","Messi", LocalDate.of(1987, 6, 24),
                10,
                true,
                repositorioSelecciones.objetos.elementAt(0),
                LocalDate.of(2005, 8, 17),
                1.70,
                72.0,
                150.0,
                Delantero),
            Jugador(
                "Cristiano",
                "Ronaldo",
                LocalDate.of(1985, 2, 5),
                7,
                true,
                repositorioSelecciones.objetos.elementAt(1),
                LocalDate.of(2003, 8, 20),
                1.87,
                84.0,
                140.0,
                Delantero),
            Jugador(
                "Sergio",
                "Ramos",
                LocalDate.of(1986, 3, 30),
                4,
                true,
                repositorioSelecciones.objetos.elementAt(2),
                LocalDate.of(2005, 3, 26),
                1.84,
                82.0,
                80.0,
                Defensor),
            Jugador(
                "Andres",
                "Iniesta",
                LocalDate.of(1984, 5, 11),
                8,
                false,
                repositorioSelecciones.objetos.elementAt(2),
                LocalDate.of(2006, 5, 27),
                1.71,
                68.0,
                70.0,
                Mediocampista,
            ),
            Jugador( "Zlatan","Ibrahimovic", LocalDate.of(1981, 11, 3),
                10,
                true,
                repositorioSelecciones.objetos.elementAt(3),
                LocalDate.of(1999, 9, 19),
                1.95,
                95.0,
                150.0,
                Delantero),
            Jugador( "Kylian ","Mbappé", LocalDate.of(1998, 12, 20),
                10,
                true,
                repositorioSelecciones.objetos.elementAt(4),
                LocalDate.of(2017, 3, 25),
                1.78,
                73.0,
                248.0,
                Delantero)
        ).forEach { repositorioJugadores.create(it) }

        arrayOf(
            Figurita(1,
                true,
                NivelImpresion.ALTA,
                repositorioJugadores.objetos.elementAt(0)),
            Figurita(
                2,
                false,
                NivelImpresion.ALTA,
                repositorioJugadores.objetos.elementAt(1)),
            Figurita(
                3,
                true,
                NivelImpresion.ALTA,
                repositorioJugadores.objetos.elementAt(2)),
            Figurita(
                5,
                false,
                NivelImpresion.ALTA,
                repositorioJugadores.objetos.elementAt(3)),
            Figurita(6,
                true,
                NivelImpresion.ALTA,
                repositorioJugadores.objetos.elementAt(4)),
        ).forEach { repositorioFiguritas.create(it) }

        val usuarios = arrayOf(
            Usuario(nombre = "Rodrigo", apellido = "Tartaglia", userName = "rodri", password = "1234",
                email = "rtartaglia@estudiantes.unsam.edu.ar", fechaDeNacimiento = LocalDate.of(2000,4,21),
                direccion = Direccion("Wakanda","Pueblo Paleta","Belgrano",2500),
                avatarUrl = "https://i.pinimg.com/1200x/da/e7/27/dae7270c586da0e3cf1db63853e5da81.jpg",
                figuritasRepetidas = mutableListOf( repositorioFiguritas.objetos.elementAt(1)),
                figuritasFaltantes = mutableListOf(
                    repositorioFiguritas.objetos.elementAt(0),
                    repositorioFiguritas.objetos.elementAt(2),
                    repositorioFiguritas.objetos.elementAt(4),
                    repositorioFiguritas.objetos.elementAt(3)
                ),
                tipoDeUsuario = Desprendido
            ),
            Usuario(nombre = "Lucas", apellido = "Antenni" ,userName = "lugant", password = "1234", avatarUrl = "https://i.imgflip.com/4m9grc.jpg",
                figuritasRepetidas = mutableListOf( repositorioFiguritas.objetos.elementAt(1),
                        repositorioFiguritas.objetos.elementAt(0)),
                figuritasFaltantes = mutableListOf( repositorioFiguritas.objetos.elementAt(2)),
                tipoDeUsuario = Desprendido
            ),
            Usuario(nombre = "Nicolas", apellido = "Masuyama",userName = "masu", password = "1234", avatarUrl = "https://i.pinimg.com/564x/36/50/ba/3650baf442939c5a3e2a260e9dcc50fb.jpg",
                figuritasRepetidas = mutableListOf( repositorioFiguritas.objetos.elementAt(2)),
                figuritasFaltantes = mutableListOf( repositorioFiguritas.objetos.elementAt(1)),
                tipoDeUsuario = Desprendido
            ),
            Usuario(nombre = "Sofia", apellido = "Morales", userName = "chofy", password = "1234", avatarUrl = "https://i.pinimg.com/564x/e1/dd/66/e1dd665089662dab5592d2a4c7ef1103.jpg",
                figuritasRepetidas = mutableListOf( repositorioFiguritas.objetos.elementAt(3)),
                figuritasFaltantes = mutableListOf( repositorioFiguritas.objetos.elementAt(0)),
                tipoDeUsuario = Desprendido
            ),
            Usuario(nombre = "Felipe", apellido = "Gonzalez", userName = "feli", password = "1234",
                email = "fgonzalezgamaleri@estudiantes.unsam.edu.ar", fechaDeNacimiento = LocalDate.of(1999,12,17),
                direccion = Direccion("Wakanda","Springfield","Belgrano",2500),
                avatarUrl = "https://media.tenor.com/ZaxXSvn0qncAAAAC/fino-se%C3%B1ores.gif",
                figuritasRepetidas = mutableListOf( repositorioFiguritas.objetos.elementAt(3),
                                                    repositorioFiguritas.objetos.elementAt(4)),
                figuritasFaltantes = mutableListOf( repositorioFiguritas.objetos.elementAt(2)),
                tipoDeUsuario = Desprendido
            )
        )
        usuarios.forEach { repositorioUsuarios.create(it) }

        arrayOf(
            Libreria("Libreria Rodri").apply {
                direccion = Direccion( localidad = "San Martin", calle = "Mitre", altura = 3436, ubicacionGeografica = Point(-34.574866, -58.536008))
                stockActual = 20
                                             },
            Supermercado("Jumbin").apply {
                direccion = Direccion( localidad = "Sidney", calle = "Wallaby", altura = 42, ubicacionGeografica = Point(-33.936072, 151.235347))
                stockActual = 74
            },
            Kiosco("Concernfour").apply {
                direccion = Direccion(localidad = "Pilar", calle = "Nazarre", altura = 1400, ubicacionGeografica = Point(-34.468467, -58.908139))
                stockActual = 62
                                        },
            //Libreria(nombre="hola", direccion = Direccion("Buenos Aires", "San Martin", "Belgrano", 5000), 423)
        ).forEach { repositorioPuntoDeVenta.create(it) }

    }
}