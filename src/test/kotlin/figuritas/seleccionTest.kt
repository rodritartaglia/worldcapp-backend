package figuritas

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeTypeOf
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class SeleccionTest: DescribeSpec({
    describe("Validacion selecciones"){
        val seleccionValida = Seleccion(
            pais = "Brasil",
            cantidadCopasConfederacion = 4,
            cantidadCopasMundiales = 5
        )
        it("Para que sea valida pais no tiene que ser vacio y cantidad de copas mayores o iguales a 0."){
            seleccionValida.esValido() shouldBe true
        }
        it("seleccion invalida"){
            val seleccionInvalida = Seleccion(
                pais = "Uruguay",
                cantidadCopasMundiales = 2,
                cantidadCopasConfederacion = -1
            )
            assertThrows<InvalidEntityException> { seleccionInvalida.validarDatos() }
        }
        it("debe especificar al menos una confederacion"){
            seleccionValida.confederacion.shouldBeTypeOf<Confederacion>()
        }
    }
})