
import tienda.modelos.*
import tienda.core.*
import tienda.data.*
import tienda.utils.*


fun main() {
    val logger = SimpleLogger("app.log")
    val inventario = Inventario(DatosMuestra().toMutableList())
    val carrito = Carrito()

    carrito.agregarListener(object : CarritoEventListener {
        override fun onItemAdded(item: ItemCarrito) { logger.info("Agregado: ${item.nombre}") }
        override fun onItemRemoved(item: ItemCarrito) { logger.info("Eliminado: ${item.nombre}") }
        override fun onCheckout(carrito: Carrito) { logger.info("Checkout total: ${carrito.total()}") }
    })

    val tasaImpuesto = 0.13

    while(true) {
        println(
            """
                ===== MENU =====
                1. Mostrar Productos
                2. Agregar al Carrito
                3. Eliminar del Carrito
                4. Ver Carrito
                5. Facturar
                6. Salir
            """.trimIndent()
        )

        print("Selecciona una opcion: ")
        when (readLine()?.toIntOrNull()) {
            1 -> inventario.imprimirProductos()
            2 -> MenuAction.agregarProducto(inventario, carrito)
            3 -> MenuAction.eliminarProducto(inventario, carrito)
            4 -> carrito.imprimirCarrito()
            5 -> MenuAction.checkout(carrito, inventario, logger, tasaImpuesto)
            6 -> return
            else -> println("Opcion invalida")
        }
    }
}