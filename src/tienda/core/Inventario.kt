package tienda.core

import tienda.modelos.Producto

class Inventario (private val productos: MutableList<Producto>) {
    fun listaProductos() = productos.toList()
    fun findById(id: Int) = productos.find { it.id == id }
    fun restarStock(id: Int, cantidad: Int): Boolean {
        val p = findById(id) ?: return false
        // agregamos validacion para el stock
        if (p.stock < cantidad) return false
        p.stock -= cantidad
        return true
    }

    fun incrementarStock(id: Int, cantidad: Int) {
        findById(id) ?.let { it.stock += cantidad }
    }

    fun imprimirProductos() {
        println("\nID | Producto | Precio | Stock")
        productos.forEach {
            println("${it.id} | ${it.nombre} | \$${"%.2f".format(it.precio)} | ${it.stock}")
        }
    }
}