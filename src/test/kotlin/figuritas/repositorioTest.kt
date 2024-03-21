package figuritas

import ar.edu.unsam.algo3.repository.Actualizador
import ar.edu.unsam.algo3.repository.Repositorio
import ar.edu.unsam.algo3.service.SeleccionesService
import ar.edu.unsam.algo3.service.SeleccionesServiceAlgoII
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RepositorioTest: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Creamos una entidad de tipo Usuario"){

        it("Se crea correctamente"){
            val usuarioValido = Usuario()
            val repositorio = Repositorio<Usuario>()
            repositorio.create(usuarioValido)
            repositorio.objetos.contains(usuarioValido) shouldBe true
        }

        it("Se borra correctamente"){
            val usuarioValido = Usuario()
            val repositorio = Repositorio<Usuario>()
            repositorio.create(usuarioValido)
            repositorio.delete(usuarioValido)
            shouldThrow<EntityNotFoundException> { repositorio.validarEstaEnColeccion(1) }
        }

        it("Se consigue el usuario buscado con getById"){
            val usuarioValido = Usuario()
            val repositorio = Repositorio<Usuario>()
            repositorio.create(usuarioValido)
            repositorio.getById(1) shouldBe usuarioValido
        }

        describe("Se busca un usuario correctamente"){
            val usuarioValido = Usuario()
            val repositorio = Repositorio<Usuario>()
            repositorio.create(usuarioValido)
            it("por nombre"){
                repositorio.search("nom") shouldBe listOf(usuarioValido)
            }
            it("por apellido"){
                repositorio.search("apell") shouldBe listOf(usuarioValido)
            }
            it("por userName exacto"){
                repositorio.search("userName") shouldBe listOf(usuarioValido)
            }
        }

        it("tira excepcion si no es nuevo"){
            val usuarioValido = Usuario()
            val repositorio = Repositorio<Usuario>()
            repositorio.create(usuarioValido)
            shouldThrow<EntityAlreadyExistsException> { repositorio.validarEsNuevo(usuarioValido) }
        }

    }

    describe("Actualizamos una entidad de tipo Seleccion"){

        it("Debe actualizar un elemento existente en la lista de objetos del repositorio") {
            val repositorio = Repositorio<Seleccion>()
            val seleccion1 = Seleccion(pais = "Argentina") //id=0
            val seleccion2 = Seleccion(pais = "Brasil") //id=1
            repositorio.create(seleccion1)
            repositorio.create(seleccion2)
            val seleccionActualizada = Seleccion(1,"Chile")
            repositorio.update(seleccionActualizada)

            repositorio.getById(1) shouldBe seleccionActualizada
        }

        it("Debe lanzar una excepcion si se intenta actualizar un elemento que no existe en el repositorio") {
            val repositorio = Repositorio<Seleccion>()
            val seleccion1 = Seleccion(pais = "Argentina") //id=0
            val seleccion2 = Seleccion(pais = "Brasil") //id=1
            repositorio.create(seleccion1)

            shouldThrow<EntityNotFoundException> { repositorio.update(seleccion2) }
        }

        it("Debe lanzar una excepcion si se actualiza un objeto con datos erroneos o faltantes"){
            val repositorio = Repositorio<Seleccion>()
            val seleccion1 = Seleccion(pais = "Argentina") //id=0
            val seleccion2 = Seleccion(pais = "Brasil") //id=1
            repositorio.create(seleccion1)
            repositorio.create(seleccion2)
            val seleccionActualizada = Seleccion(1,"")

            shouldThrow<InvalidEntityException> { repositorio.update(seleccionActualizada) }
        }
    }

    describe("Probamos el Interfaz de Service Selecciones"){

        val json =
                    """
                [
          {
            "id": 1,
            "pais": "Argentina",
            "confederacion": "CONMEBOL",
            "copasDelMundo": 3,
            "copasConfederacion": 15
          },
          {
            "id": 2,
            "pais": "Brasil",
            "confederacion": "CONMEBOL",
            "copasDelMundo": 5,
            "copasConfederacion": 9
          },
          {
            "pais": "Alemania",
            "confederacion": "UEFA",
            "copasDelMundo": 4,
            "copasConfederacion": 3
          },
          {
            "id": 3,
            "pais": "Mexico",
            "confederacion": "CONCACAF",
            "copasDelMundo": 0,
            "copasConfederacion": 1
          }
        ]
        """



        it("Probamos que Alemania se crea correctamente"){
            val repositorio = Repositorio<Seleccion>()
            val seleccion1 = Seleccion(pais = "Argentina")
            val seleccion2 = Seleccion(pais = "Brasil")
            val seleccion3 = Seleccion(pais = "Mexico")
            val actualizador = Actualizador().apply{
                repoAActualizar = repositorio
                service = StubService(json)
            }

            repositorio.create(seleccion1)
            repositorio.create(seleccion2)
            repositorio.create(seleccion3)

            actualizador.actualizar()

            repositorio.getById(4).pais shouldBe "Alemania"
            repositorio.getById(4).confederacion shouldBe Confederacion.UEFA
            repositorio.getById(4).cantidadCopasMundiales shouldBe 4
            repositorio.getById(4).cantidadCopasConfederacion shouldBe 3
        }

        it("Comprobamos que Argentina, Brasil y Mexico se actualizaron correctamente"){

            val repositorio = Repositorio<Seleccion>()
            val seleccion1 = Seleccion(pais = "Argentina")
            val seleccion2 = Seleccion(pais = "Brasil")
            val seleccion3 = Seleccion(pais = "Mexico")
            val actualizador = Actualizador().apply{
                repoAActualizar = repositorio
                service = StubService(json)
            }

            repositorio.create(seleccion1)
            repositorio.create(seleccion2)
            repositorio.create(seleccion3)

            actualizador.actualizar()


            repositorio.getById(1).cantidadCopasConfederacion shouldBe 15
            repositorio.getById(2).cantidadCopasMundiales shouldBe 5
            repositorio.getById(3).pais shouldBe "Mexico"
            repositorio.getById(3).confederacion shouldBe Confederacion.CONCACAF
        }
    }
})

class StubService(private val json: String) : SeleccionesServiceAlgoII {
    override fun getSelecciones(): String {
        return json
    }
}
