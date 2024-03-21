package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.entity.dto.FiguritaDTO
import ar.edu.unsam.algo3.entity.dto.UsuarioDTO
import ar.edu.unsam.algo3.entity.dto.UsuarioFormDTO
import ar.edu.unsam.algo3.service.UsuarioJSON
import ar.edu.unsam.algo3.service.UsuarioService
import figuritas.Usuario
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("usuario")
@CrossOrigin("*")
class UsuarioController(
    val usuarioService: UsuarioService
) {

    @GetMapping("id/{idUsuario}")
    @Operation(summary = "Devuelve el usuario para el id dado")
    fun getUsuarioById(@PathVariable idUsuario: Int): UsuarioDTO = UsuarioDTO(usuarioService.getInfoUsuario(idUsuario))

    @GetMapping("id/form/{idUsuario}")
    @Operation(summary = "Devuelve los datos del usuario para el formulario")
    fun getUsuarioFormById(@PathVariable idUsuario: Int): UsuarioFormDTO = UsuarioFormDTO(usuarioService.getInfoUsuarioForm(idUsuario))

//    @GetMapping("id/{idUsuario}/{password}")
//    @Operation(summary = "Devuelve si la contraseña es correcta")
//    fun getVerifiedPasswordById(@PathVariable idUsuario: Int, @PathVariable password: String): Boolean = usuarioService.verifyPassword(idUsuario,password)

    @GetMapping("usuarios")
    @Operation(summary = "Devuelve todos los usuarios")
    fun getUsuarios(): List<UsuarioDTO> = usuarioService.getUsuarios().map { UsuarioDTO(it) }

    //@GetMapping("id/{idUsuario}/basic")
    //@Operation(summary = "Devuelve la info basica de perfil para el id dado")
    //fun getUsuarioBasicoById(@PathVariable idUsuario: Int): UsuarioDTO = UsuarioDTO(usuarioService.getInfoUsuario(idUsuario))

    @PatchMapping("id/{idUsuario}")
    @Operation(summary = "Permite actualizar al usuario")
    fun actualizarUsuario(@PathVariable idUsuario: Int, @RequestBody usuarioBody : UsuarioFormDTO){
        usuarioService.actualizarUsuario(idUsuario, usuarioBody)
    }

    @PostMapping("id/{idUsuario}")
    @Operation(summary = "Permite iniciar sesion y cargar lo minimo para la pagina principal")
    fun iniciarSesion(@PathVariable idUsuario: Int): Nothing = TODO()


//    @GetMapping("id/{idUsuario}/{idFigurita}")
//    @Operation(summary = "permite hacer el tradeo de una figurita")
//    fun pedirFigurita(@PathVariable idUsuario: Int, idFigurita: Int) = usuarioService.intercambiarFigurita(idUsuario,idFigurita

    @PostMapping("/login")
    @Operation(summary = "Devuelve el id si la contraseña y el usuario es correcto")
    fun getVerifiedUser(@RequestParam user: String, @RequestParam password: String): Int = usuarioService.verifyUser(user,password)

}