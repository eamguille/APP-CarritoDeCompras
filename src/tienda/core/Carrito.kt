package tienda.core

import tienda.modelos.ItemCarrito
import tienda.modelos.Producto

class Carrito {
    private val items = mutableListOf<ItemCarrito>()
    private val listeners = mutableListOf<CarritoEventListener>()

    fun agregarListener(listener: CarritoEventListener) = listeners.add(listener)
    fun listaItems() = items.toList()
    fun isEmpty() = items.isEmpty()
    fun total() = items.sumOf { it.subTotal() }

    fun agregarItem(producto: Producto, cantidad: Int) {
        val existente = items.find { it.IdProducto == producto.id }
        if(existente != null) {
            existente.cantidad += cantidad
            listeners.forEach { it.onItemAdded(existente) }
        } else {
            val nuevoItem = ItemCarrito(producto.id, producto.nombre, producto.precio, cantidad)
            items.add(nuevoItem)
            listeners.forEach { it.onItemAdded(nuevoItem) }
        }
    }

    fun eliminarItem(IdProducto: Int, cantidad: Int):ItemCarrito? {
        val existente = items.find { it.IdProducto == IdProducto } ?: return null
        return if(cantidad >= existente.cantidad) {
            items.remove(existente)
            listeners.forEach { it.onItemRemoved(existente) }
            existente
        } else {
            existente.cantidad -= cantidad
            val eliminado = ItemCarrito(existente.IdProducto, existente.nombre, existente.precio, cantidad)
            listeners.forEach { it.onItemRemoved(eliminado) }
            eliminado
        }
    }

    fun limpiarCarrito() = items.clear()

    fun checkout(): Double {
        listeners.forEach { it.onCheckout(this) }
        val tot = total()
        items.clear()
        return tot
    }

    fun imprimirCarrito() {
        if (items.isEmpty()) {
            println("Carrito vacio")
            return
        }

        println("\nID | Producto | Precio/u | Cantidad | SubTotal")
        items.forEach {
            println("${it.IdProducto} | ${it.nombre} | \$${"%.2f".format(it.precio)} | ${it.cantidad} | \$${"%.2f".format(it.subTotal())}")
        }

        println("\nTotal General: \$${"%.2f".format(items.sumOf { it.subTotal() })}")
    }
}