package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.entity.dto.UsuarioDTO
import ar.edu.unsam.algo3.service.GeneralService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("")
@CrossOrigin("*")
class GeneralController(
    val generalService: GeneralService
){

    @GetMapping("usuariosActivos")
    @Operation(summary = "Obtiene los usuarios activos de la aplicacion")
    fun getUsuariosActivos() = generalService.getUsuarioActivos().map { UsuarioDTO(it) }

    @GetMapping("homeInfo")
    @Operation(summary = "Obtenemos figuritas relables, faltantes, puntos de venta y usuarios activos")
    fun getHomeInfo() = mapOf<String, Int>(
        "figuritasRegalables" to generalService.getCantidadFiguritasRegalables(),
        "figuritasFaltantes" to generalService.getCantidadFiguritasFaltantes(),
        "puntosDeVenta" to generalService.getCantidadPuntosDeVenta(),
        "usuariosActivos" to generalService.getCantidadUsuariosActivos()
    )

    @GetMapping("posiciones")
    @Operation(summary = "Obtiene las posiciones")
    fun getPosiciones() = generalService.getPosiciones()

}