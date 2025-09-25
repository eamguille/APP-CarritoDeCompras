package tienda.core

import tienda.modelos.*
import tienda.utils.*
import tienda.data.*

object MenuAction {

    // Para agregar el producto al carrito
    fun agregarProducto(inventario: Inventario, carrito: Carrito) {
        inventario.imprimirProductos()
        print("Ingresa el ID del producto a agregar: ")
        val id = readLine()?.toIntOrNull()
        if(id == null) {
            println("Entrada invalida")
            return
        }

        val producto = inventario.findById(id)
        if(producto == null) {
            println("Producto no fue encontrado.")
            return
        }

        print("Cantidad a agregar: ")
        val cantidad = readLine()?.toIntOrNull()
        if(cantidad == null || cantidad <= 0) {
            println("Cantidad invalida.")
            return
        }
        if(producto.stock < cantidad) {
            println("Stock insuficiente. Disponibles: ${producto.stock}")
        }

        val ok = inventario.restarStock(id, cantidad)
        if(!ok) {
            println("Error al reservar stock.")
            return
        }

        carrito.agregarItem(producto, cantidad)
        println("Se agrego $cantidad x ${producto.nombre} al carrito.")
    }

    fun eliminarProducto(inventario: Inventario, carrito: Carrito) {
        if(carrito.isEmpty()) {
            println("Carrito vacio.")
            return
        }

        carrito.imprimirCarrito()
        print("Ingresa el ID del producto a eliminar: ")
        val id = readLine()?.toIntOrNull()
        if(id == null) {
            println("Entrada invalida.")
            return
        }

        val existente = carrito.listaItems().find { it.IdProducto == id }
        if(existente == null) {
            println("Este producto no esta en el carrito.")
            return
        }

        print("Cantidad a eliminar (max ${existente.cantidad}): ")
        val cantidad = readLine()?.toIntOrNull()
        if(cantidad == null || cantidad <= 0) {
            println("Cantidad invalida.")
            return
        }

        val eliminado = carrito.eliminarItem(id, cantidad)
        if(eliminado != null) {
            inventario.incrementarStock(id, eliminado.cantidad)
            println("Eliminado ${eliminado.cantidad} x ${eliminado.nombre} del carrito.")
        } else {
            println("Producto no pudo ser eliminado del carrito.")
        }
    }

    fun checkout(carrito: Carrito, inventario: Inventario, logger: SimpleLogger, tasa: Double) {
        if(carrito.isEmpty()) {
            println("Carrito vacio.")
            return
        }

        // copiamos el estado antes de facturar
        val itemsAntes = carrito.listaItems()
        val factura = GeneradorFactura.generador(carrito, tasa)

        println("\n$factura")
        print("Confirmar compra? (S/N): ")
        val respuesta = readLine()?.trim()?.lowercase()

        if(respuesta == "s" || respuesta == "si") {
            val total = carrito.checkout()
            logger.info("Compra confirmada. Total: $total")
            logger.info("Factura:\n$factura")
            println("Compra realizada con exito.")
        } else {
            // Restauramos el inventario y vaciamos el carrito
            itemsAntes.forEach { inventario.incrementarStock(it.IdProducto, it.cantidad) }
            carrito.limpiarCarrito()
            logger.info("Compra cancelada por usuario. Stock restaurado")
            println("Compra cancelada. Carrito vaciado y stock restaurado.")
        }
    }
}