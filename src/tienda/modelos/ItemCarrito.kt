package tienda.modelos

data class ItemCarrito(val IdProducto: Int, val nombre: String, val precio: Double, var cantidad: Int) {
    fun subTotal() = precio * cantidad
}