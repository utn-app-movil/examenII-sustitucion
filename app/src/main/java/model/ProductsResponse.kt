package cr.ac.utn.appmovil.products.model

data class ProductsResponse(
    val data: List<Product>?,
    val responseCode: String,
    val message: String
)