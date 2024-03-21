package figuritas

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit


fun Int.esPar()= this.mod(2) == 0

fun Int.noEsPar()= this.mod(2) != 0

fun LocalDate.comparoAlPresente() = ChronoUnit.YEARS.between(this, LocalDate.now())

class InvalidEntityException(msg: String = "Los datos ingresados son erroneos o hay datos faltantes."): RuntimeException(msg)
class EntityAlreadyExistsException(msg: String = "No es posible agregar un elemento ya existente."): RuntimeException(msg)

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFoundException(id: Int,msg: String = "No se encontro ningun objeto con el id $id."): RuntimeException(msg)

class UserAlreadyHasFiguritaException(msg: String = "Al usuario no le falta la figurita"): RuntimeException(msg)

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class FiguritaCantBeGivenException(msg: String = "El usuario no puede regalar esa figurita"): RuntimeException(msg)

@ResponseStatus(HttpStatus.CONFLICT)
class CantBeDeletedException(reasons:MutableList<String> ,msg: String = "No es posible borrar la figurita por los siguiente" +
        "motivos: ${reasons.toString()}"): RuntimeException(msg)

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class TargetIsTooFarException(msg: String = "No es posible de borrarlo"): RuntimeException(msg)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class LoginException(username: String, msg:String = "El usuario $username no existe o no es valida la contraseña"): RuntimeException(msg)
