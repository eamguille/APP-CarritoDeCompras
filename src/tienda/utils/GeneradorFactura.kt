package tienda.utils

import tienda.core.Carrito
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GeneradorFactura {
    fun generador(carrito: Carrito, tasas: Double):String {
        val sb = StringBuilder()
        sb.append("======= FACTURA =======")
        sb.append("Fecha: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}\n\n")
        carrito.listaItems().forEach {
            sb.append("${it.nombre} x${it.cantidad} = \$${it.subTotal()}\n")
        }

        val subtotal = carrito.total()
        val tasa = subtotal * tasas
        val total = subtotal + tasa
        sb.append("\nSubTotal: \$${"%.2f".format(subtotal)}")
        sb.append("\nImpuesto: \$${"%.2f".format(tasa)}")
        sb.append("\nTotal: \$${"%.2f".format(total)}")

        return sb.toString()
    }
}