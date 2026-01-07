package cr.ac.utn.appmovil.products.model

data class CategoriesResponse(
    val data: List<Category>?,
    val responseCode: String,
    val message: String
)