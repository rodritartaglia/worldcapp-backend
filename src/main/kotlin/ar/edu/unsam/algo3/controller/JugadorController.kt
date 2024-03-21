package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.entity.dto.FiguritaDTO
import ar.edu.unsam.algo3.entity.dto.JugadorDTO
import ar.edu.unsam.algo3.service.JugadorService
import figuritas.Jugador
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("jugador")
@CrossOrigin("*")
class JugadorController(
    val jugadorService: JugadorService,
) {

    @GetMapping("jugadores")
    @Operation(summary = "Obtener jugadores")
    fun getJugadores(): List<JugadorDTO> = jugadorService.getJugadores().map {JugadorDTO(it)}

    @GetMapping("nombresJugadores")
    @Operation(summary = "Obtener el nombre de los jugadores")
    fun getNombreJugadores()= jugadorService.getJugadores().map {it.nombre +" "+ it.apellido}

    @PostMapping("agregar")
    @Operation(summary = "Permite agregar un jugador")
    fun agregarJugador(@RequestBody jugadorJSON: JugadorDTO) = jugadorService.agregarJugador(jugadorJSON)

    @GetMapping("buscar")
    @Operation(summary = "Busca jugadores")
    fun buscarJugador(@RequestParam search: String) = jugadorService.buscar(search).map{JugadorDTO(it)}

    @GetMapping("{id}")
    @Operation(summary = "Obtener figurita por id")
    fun figuritaById(@PathVariable id: Int): Jugador = jugadorService.getJugador(id)

    @PatchMapping("modificar")
    @Operation(summary = "Permite modificar un jugador")
    fun modificarJugador(@RequestBody JugadorJSON: JugadorDTO) = jugadorService.modificarJugador(JugadorJSON)

}