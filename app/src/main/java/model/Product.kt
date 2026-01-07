package cr.ac.utn.appmovil.products.model

data class Product(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val cantidad: Int,
    val fotoPath: String?
)