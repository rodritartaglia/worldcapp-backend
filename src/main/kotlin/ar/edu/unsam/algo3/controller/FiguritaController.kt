package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.service.FiguritaService
import ar.edu.unsam.algo3.entity.dto.FiguritaDTO
import figuritas.Figurita
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*


@RestController()
@RequestMapping("figurita")
@CrossOrigin("*")
class FiguritaController(
    val figuritaService: FiguritaService
) {

    @GetMapping("busquedaFiguritas/{idUsuario}")
    @Operation(summary = "Devuelve todas las figuritas con sus respectivos filtros")
    fun getFiguritasSolicitablesForUsuarios(
        @PathVariable idUsuario: Int,
        @RequestParam(name = "min" , required = false) rangoMin : Int? = 0,
        @RequestParam(name="max", required = false) rangoMax : Int? = 1000,
        @RequestParam (name="onFire", required=false) onFire : Boolean,
        @RequestParam(name="promesa", required = false) promesa : Boolean,
        @RequestParam(name="busqueda", required = false) busqueda: String?
    ) : List<FiguritaDTO> = figuritaService.getFiguritasSolicitablesForUsuarios(rangoMin, rangoMax, onFire, promesa, idUsuario, busqueda)
        .mapValues { (usuario, figuritas)  -> figuritas.map{ FiguritaDTO(it, usuario) }}.values.flatten()

    @GetMapping("{idUsuario}/{idFigurita}")
    @Operation(summary = "Devuelve una figurita dado un id especifico")
    fun getFiguritaById(@PathVariable idUsuario: Int ,@PathVariable idFigurita : Int) : FiguritaDTO = FiguritaDTO(figuritaService.getFigurita(idFigurita), figuritaService.getUsuario(idUsuario))

    @GetMapping("figuritas/search/{parametroABuscar}")
    @Operation(summary = "Devuelve todas las figuritas que concuerden con el nombre o apellido pasado por parametro")
    fun buscarFigurita(@PathVariable parametroABuscar : String) : List<FiguritaDTO> = figuritaService.buscarFigurita(parametroABuscar).map{ FiguritaDTO(it) }

    //METODOS TRAÍDOS DE USUARIOCONTROLLER

    @GetMapping("id/{idUsuario}/repetidas")
    @Operation(summary = "Devuelve las figuritas repetidas de un usuario")
    fun getFiguritasRepetidas(@PathVariable idUsuario: Int): List<FiguritaDTO> = figuritaService.getFiguritasRepetidas(idUsuario).map { FiguritaDTO(it) }

    @GetMapping("id/{idUsuario}/faltantes")
    @Operation(summary = "Devuelve las figuritas faltantes de un usuario")
    fun getFiguritasFaltantes(@PathVariable idUsuario: Int) : List<FiguritaDTO> = figuritaService.getFiguritasFaltantes(idUsuario).map { FiguritaDTO(it) }

    @DeleteMapping("id/{idUsuario}/repetidas/{idFigurita}")
    @Operation(summary = "Permite eliminar una figurita repetida de un usuario especifico")
    fun borrarFiguritaRepetida(@PathVariable idUsuario: Int, @PathVariable idFigurita : Int) = figuritaService.borrarFiguritaRepetida(idUsuario, idFigurita)

    @DeleteMapping("id/{idUsuario}/faltantes/{idFigurita}")
    @Operation(summary = "Permite eliminar una figurita faltante de un usuario especifico")
    fun borrarFiguritaFaltante(@PathVariable idUsuario: Int, @PathVariable idFigurita : Int) = figuritaService.borrarFiguritaFaltante(idUsuario, idFigurita)

    @PostMapping("id/{idUsuario}/repetidas")
    @Operation(summary = "Permite agregar una figurita repetida de un usuario especifico")
    fun agregarFiguritaRepetida(@PathVariable idUsuario: Int, @RequestBody idFigurita : Int) = figuritaService.agregarFiguritaRepetida(idUsuario, idFigurita)

    @PostMapping("id/{idUsuario}/faltantes")
    @Operation(summary = "Permite agregar una figurita faltante de un usuario especifico")
    fun agregarFiguritaFaltante(@PathVariable idUsuario: Int, @RequestBody idFigurita : Int) = figuritaService.agregarFiguritaFaltante(idUsuario, idFigurita)

    @PatchMapping("intercambiarFigurita/{idUsuario}/{idDuenio}/{idFigurita}")
    @Operation(summary = "Intercambia una figurita")
    fun solicitarFigurita(@PathVariable idUsuario: Int, @PathVariable idDuenio: Int, @PathVariable idFigurita: Int): Unit {
        figuritaService.solicitarFigurita(idUsuario, idDuenio, idFigurita)
    }

    @GetMapping("figuritas")
    @Operation(summary = "Devuelve todas las figuritas del sistema")
    fun obtenerTodasLasFiguritas(): List<FiguritaDTO> = figuritaService.getAllFiguritas().map { FiguritaDTO(it) }

    @PostMapping("agregar")
    @Operation(summary = "Crea una nueva figurita en el sistema")
    fun crearNuevaFigurita(@RequestBody figuritaJSON : Figurita) = figuritaService.crearFigurita(figuritaJSON)

    @DeleteMapping("borrar/{id}")
    @Operation(summary = "Elimina una figurita")
    fun eliminarFigurita(@PathVariable id : Int) = figuritaService.eliminarFigurita(id)

    @PatchMapping("modificar")
    @Operation(summary = "Permite modificar una figurita")
    fun modificarFigurita(@RequestBody patchContent : Figurita) = figuritaService.modificarFigu(patchContent)

    @GetMapping("{id}")
    @Operation(summary = "Obtener figurita por id")
    fun figuritaById(@PathVariable id: Int): Figurita = figuritaService.getFigurita(id)

    @DeleteMapping("borrarJugador/{id}")
    @Operation(summary = "Permite eliminar un jugador")
    fun deleteJugador(@PathVariable id: Int) = figuritaService.eliminarJugador(id)



}