package cr.ac.utn.appmovil.products.model

data class ProductRequest(
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val cantidad: Int,
    val fotoBase64: String? = null
)