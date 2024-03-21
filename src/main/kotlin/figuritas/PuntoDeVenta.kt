package figuritas

import ar.edu.unsam.algo3.repository.Entidad
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.ceil
import kotlin.math.round

abstract class PuntoDeVenta(
    open var nombre: String = "#",
    var direccion: Direccion = Direccion(),
    var stockActual: Int = 0,
    var pedidos: MutableList<PedidoAFabrica> = mutableListOf<PedidoAFabrica>(),
    override var id: Int = 0

) : Entidad {

    companion object {
        val DISTANCIA_MAXIMA: Double = 10.0 //en km
        val COSTO_MINIMO: Double = 170.0
        val COSTO_MINIMO_DISTANCIA: Double = 1000.0
        val ADICIONAL_POR_KM_EXTRA: Double = 100.0
    }

    fun getDireccion(): String = direccion.getCalleConAltura()

    fun getUbicacionGeografica(): String = direccion.getUbicacionGeografica()

    fun disponibilidad() = stockActual != 0

    fun importeTotal(cliente: Usuario, sobres: Int): Double = round(importeACobrar(sobres,cliente.direccion) * porcentajeDelPuntoDeVenta(sobres))

    fun importeACobrar(sobres: Int, direccion: Direccion): Double = sobres * COSTO_MINIMO + importePorDistancia(direccion)

    abstract fun porcentajeDelPuntoDeVenta(cantidadDeSobres: Int): Double

    fun importePorDistancia(direccionDelCliente: Direccion): Double = COSTO_MINIMO_DISTANCIA + extraPorSuperarDistanciaMaxima(direccionDelCliente)

    fun extraPorSuperarDistanciaMaxima(direccionDelCliente: Direccion): Double = if (superaDistancia(direccionDelCliente)) calculoSiSuperaDistancia(direccionDelCliente) else 0.0

    fun calculoSiSuperaDistancia(direccionDelCliente: Direccion): Double = ADICIONAL_POR_KM_EXTRA * excesoDeDistancia(direccionDelCliente)

    fun superaDistancia(direccionDelCliente: Direccion) = direccion.calcularCercania(direccionDelCliente) > DISTANCIA_MAXIMA

    fun excesoDeDistancia(direccionDelCliente: Direccion) = ceil(direccion.calcularCercania(direccionDelCliente) - DISTANCIA_MAXIMA)

    fun pedidosPendientes(): List<PedidoAFabrica> =  pedidos.filter{ it.esPedidoPendiente()}

    fun cantidadPedidosPendientes(): Int = pedidosPendientes().size

    fun pedidosEntregados(): List<PedidoAFabrica> =  pedidos.filter{ it.cuantosDiasQuedanParaElIngreso() < 0 }

    fun actualizar(){
        sumarStock()
        quitarPedidosYaEntregados()
    }

    private fun quitarPedidosYaEntregados() {
        pedidos = (pedidos - pedidosEntregados().toSet()).toMutableList()
    }

    private fun sumarStock() {
        stockActual += pedidosEntregados().sumOf { it.cantidadDeSobresARecibir }
    }

    override fun condicionDeBusqueda(value: String) = nombre.lowercase().contains(value)

    override fun esValido(): Boolean {
        return nombre.isNotBlank() &&
                direccion.esValido() &&
                stockActual >= 0
    }


}

class PedidoAFabrica(
    val cantidadDeSobresARecibir: Int = 0,
    val fechaPactada: LocalDate = LocalDate.now()
){
    companion object{
        val DIAS_MAXIMOS: Int = 10
    }

    fun cuantosDiasQuedanParaElIngreso() = ChronoUnit.DAYS.between(fechaPactada,LocalDate.now())

    fun esPedidoPendiente() = cuantosDiasQuedanParaElIngreso() < DIAS_MAXIMOS


}

class Kiosco(override var nombre :String = "Kiosco") : PuntoDeVenta() {

    var esAtendidoPorEmpleado: Boolean = false //si es falso, está atendido por el jefe

    override fun porcentajeDelPuntoDeVenta(cantidadDeSobres: Int): Double = if (esAtendidoPorEmpleado) 1.25 else  1.10

}

class Libreria(override var nombre :String = "Libreria"): PuntoDeVenta() {

    val EXTRA_SIN_PEDIDOS_PENDIENTES: Double = 1.10
    val EXTRA_CON_PEDIDOS_PENDIENTES: Double = 1.05

    fun noTienePedidosPendientes() = pedidosPendientes().isEmpty()
    override fun porcentajeDelPuntoDeVenta(cantidadDeSobres: Int): Double = if(noTienePedidosPendientes()) EXTRA_SIN_PEDIDOS_PENDIENTES else EXTRA_CON_PEDIDOS_PENDIENTES

}

class Supermercado(override var nombre : String = "Supermercado"): PuntoDeVenta(){

    var tipoDeDescuento: Descuento = SinDescuento

    override fun porcentajeDelPuntoDeVenta(cantidadDeSobres: Int): Double = tipoDeDescuento.descuento(cantidadDeSobres)

}

interface Descuento{

    fun PORCENTAJE_DE_DESCUENTO(): Double

    fun condicionDeDescuento(cantidadDeSobres: Int): Boolean

    fun descuento(cantidadDeSobres: Int) =
        conversionDeDescuento(calculoDeDescuentoCrudo(cantidadDeSobres))

    fun calculoDeDescuentoCrudo(cantidadDeSobres: Int): Double =
        if (condicionDeDescuento(cantidadDeSobres)) PORCENTAJE_DE_DESCUENTO() else 0.0

    fun conversionDeDescuento(porcenajeEnCrudo: Double) =  1.0 - porcenajeEnCrudo / 100.0
    //ejemplo: 45% de descuento -> n * 0.55
    //Lo convierte en algo multiplicable a otro numero


}

object DescuentoDiaDeLaSemana: Descuento{ //Se hizo un stub de este objeto
    override fun PORCENTAJE_DE_DESCUENTO(): Double = 10.0
    override fun condicionDeDescuento(cantidadDeSobres: Int): Boolean = condicionDeDescuentoPorDia()

    fun condicionDeDescuentoPorDia(): Boolean = diaDeLaSemana() == diaDelDescuento()

    fun diaDelDescuento(): DayOfWeek = DayOfWeek.THURSDAY //el dia de la semana que usa el negocio es el jueves

    fun diaDeLaSemana(): DayOfWeek = LocalDate.now().dayOfWeek

}

object DescuentoPrimerosDiasDelMes: Descuento{ //Se hizo un stub de este objeto

    val FECHA_DE_INICIO: Int = 1
    val FECHA_DE_FIN: Int = 10
    override fun PORCENTAJE_DE_DESCUENTO(): Double = 5.0

    override fun condicionDeDescuento(cantidadDeSobres: Int): Boolean = diaDelMes() in FECHA_DE_INICIO..FECHA_DE_FIN

    fun diaDelMes() = LocalDate.now().dayOfMonth

}


object DescuentoCompraAlMayor: Descuento{

    val CANTIDAD_DE_SOBRES_PARA_DESCUENTO: Int = 200
    override fun PORCENTAJE_DE_DESCUENTO(): Double = 45.0
    override fun condicionDeDescuento(cantidadDeSobres: Int): Boolean = cantidadDeSobres > CANTIDAD_DE_SOBRES_PARA_DESCUENTO
}

object SinDescuento: Descuento{

    override fun PORCENTAJE_DE_DESCUENTO(): Double = 0.0
    override fun condicionDeDescuento(cantidadDeSobres: Int): Boolean = true

}

class DescuentosAcumulados(val descuentos: MutableSet<Descuento>): Descuento{

    override fun PORCENTAJE_DE_DESCUENTO(): Double = 50.0
    override fun condicionDeDescuento(cantidadDeSobres: Int): Boolean = true

    override fun calculoDeDescuentoCrudo(cantidadDeSobres: Int): Double =
        minOf(sumatoriaDeDescuentosCrudos(cantidadDeSobres), PORCENTAJE_DE_DESCUENTO())

    fun sumatoriaDeDescuentosCrudos(cantidadDeSobres: Int):Double = descuentos.sumOf { it.calculoDeDescuentoCrudo(cantidadDeSobres) }

}