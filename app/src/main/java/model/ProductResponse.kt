package cr.ac.utn.appmovil.products.model

data class ProductResponse(
    val data: Product?,
    val responseCode: String,
    val message: String
)