package cr.ac.utn.appmovil.products.model

data class AuthRequest(
    val username: String,
    val password: String
)

data class User(
    val id: Int?,
    val username: String,
    val role: String?
)
