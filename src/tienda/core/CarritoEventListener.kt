package tienda.core

import tienda.modelos.ItemCarrito

interface CarritoEventListener {
    fun onItemAdded(item: ItemCarrito)
    fun onItemRemoved(item: ItemCarrito)
    fun onCheckout(carrito: Carrito)
}