package cr.ac.utn.appmovil.products.model

import com.google.gson.annotations.SerializedName

data class GenericResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)
