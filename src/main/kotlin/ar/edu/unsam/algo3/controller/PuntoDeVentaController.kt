package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.entity.dto.PuntoDeVentaDTO
import ar.edu.unsam.algo3.service.PuntoDeVentaService
import io.swagger.v3.oas.annotations.Operation
import figuritas.PuntoDeVenta
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("puntoDeVenta")
@CrossOrigin("*")
class PuntoDeVentaController(
    val puntoDeVentaService: PuntoDeVentaService
) {

    @GetMapping("")
    @Operation(summary = "Devuelve todos los puntos de venta")
    fun getPuntosDeVenta() : List<PuntoDeVentaDTO> = puntoDeVentaService.getPuntosDeVenta().map { PuntoDeVentaDTO(it) }

//    @GetMapping("{idUsuario}")
//    @Operation(summary = "Devuelve todos los puntos de venta respecto a un usuario")
//    fun getPuntosDeVentaPorUsuario(@PathVariable idUsuario: Int) : List<PuntoDeVentaDTO> = puntoDeVentaService.getPuntosDeVentaRespectoAUsuario(idUsuario).mapValues { (usuario, puntosDeVenta) ->  puntosDeVenta.map { PuntoDeVentaDTO(it, usuario) } }.values.flatten()

    @GetMapping("{idUsuario}")
    @Operation(summary = "Devuelve todos los puntos de venta respecto a un usuario y una busqueda")
    fun getPuntosDeVentaBusqueda(@PathVariable idUsuario: Int, @RequestParam busqueda: String?): List<PuntoDeVentaDTO> =  puntoDeVentaService.buscarPuntoDeVenta(idUsuario, busqueda)
        .mapValues { (usuario, puntosDeVenta) -> puntosDeVenta.map { PuntoDeVentaDTO(it, usuario) } }.values.flatten()

    @PostMapping("agregar")
    @Operation(summary = "Permite agregar un punto de venta")
    fun agregarPuntoDeVenta(@RequestBody puntoDeVentaJSON: PuntoDeVentaDTO){
        puntoDeVentaService.agregarPuntoDeVenta(puntoDeVentaJSON)
    }

    @DeleteMapping("borrar/{id}")
    @Operation(summary = "Permite eliminar un punto de venta")
    fun deletePuntoDeVenta(@PathVariable id: Int){
        puntoDeVentaService.eliminarPuntoDeVenta(id)
    }

    @PatchMapping("modificar")
    @Operation(summary = "Permite modificar un punto de venta")
    fun modificarPuntoDeVenta(@RequestBody puntoDeVentaJSON: PuntoDeVentaDTO){
        puntoDeVentaService.modificarPuntoDeVenta(puntoDeVentaJSON)
    }

    @GetMapping("tipos")
    @Operation(summary = "Devuelve los tipos de puntos de venta")
    fun tiposDePuntoDeVenta() = puntoDeVentaService.tipos()

    @GetMapping("buscar")
    @Operation(summary = "Busca los puntos de venta por un string")
    fun buscarPuntoDeVenta(@RequestParam search: String) = puntoDeVentaService.buscarPuntoDeVenta(search).map { PuntoDeVentaDTO(it) }

}