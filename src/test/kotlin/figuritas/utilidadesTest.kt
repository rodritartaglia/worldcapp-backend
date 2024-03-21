package figuritas

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UtilidadesTest: DescribeSpec({
    describe("probamos nuevas utilidades del Int"){
        it("Numero par"){
            val numeroPar = 2
            numeroPar.esPar().shouldBeTrue()
            numeroPar.noEsPar().shouldBeFalse()
        }

        it("Numero impar"){
            val numeroImpar = 3
            numeroImpar.esPar().shouldBeFalse()
            numeroImpar.noEsPar().shouldBeTrue()
        }
    }

    describe("probamos nuevas utilidades del LocalDate"){
        val fechaDeReferencia = LocalDate.of(1987,6,24)
        it ("comparo anios en UnitTime"){
            fechaDeReferencia.comparoAlPresente().shouldBe(ChronoUnit.YEARS.between(fechaDeReferencia, LocalDate.now()))
        }
        }

})