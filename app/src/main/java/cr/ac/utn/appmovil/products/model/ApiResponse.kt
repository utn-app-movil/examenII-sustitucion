package cr.ac.utn.appmovil.products.model

data class ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)
