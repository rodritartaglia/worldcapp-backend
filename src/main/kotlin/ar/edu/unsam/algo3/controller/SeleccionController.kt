package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.entity.dto.SeleccionDTO
import ar.edu.unsam.algo3.service.SeleccionesService
import figuritas.PuntoDeVenta
import figuritas.Seleccion
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("seleccion")
@CrossOrigin("*")
class SeleccionController (
    val seleccionService: SeleccionesService
){

    @GetMapping("selecciones")
    @Operation(summary = "Obtener los nombres de las selecciones")
    fun getSelecciones() = seleccionService.getSelecciones().map { SeleccionDTO(it) }

    @GetMapping("nombres")
    @Operation(summary = "Obtener los nombres de las selecciones")
    fun getNombresSelecciones() = seleccionService.getNombres()

    @GetMapping("confederaciones")
    @Operation(summary = "Obten todas las confederaciones")
    fun getConfederaciones() = seleccionService.getConfederaciones()

    @PostMapping("agregar")
    @Operation(summary = "agregar seleccion")
    fun agregarSeleccion(@RequestBody seleccionJSON: Seleccion) = seleccionService.agregarSeleccion(seleccionJSON)

    @PatchMapping("modificar")
    @Operation(summary = "modificar seleccion")
    fun modificarSeleccion(@RequestBody seleccionJSON: Seleccion) = seleccionService.modificarSeleccion(seleccionJSON)

    @DeleteMapping("borrar/{id}")
    @Operation(summary = "borrar seleccion")
    fun borrarSeleccion(@PathVariable id: Int) = seleccionService.borrarSeleccion(id)

}