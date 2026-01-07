package cr.ac.utn.appmovil.products.model

data class AuthResponse(
    val data: User?,
    val responseCode: String,
    val message: String
)