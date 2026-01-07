package cr.ac.utn.appmovil.products.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoria") val name: String
)
