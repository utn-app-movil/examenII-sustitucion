package cr.ac.utn.appmovil.products.model

import com.google.gson.annotations.SerializedName

data class Product(
    var id: Int? = null,
    @SerializedName("nombre") var name: String,
    @SerializedName("categoria") var category: String,
    @SerializedName("precio") var price: Double,
    @SerializedName("cantidad") var quantity: Int,
    var fotoPath: String? = null,
    var fotoBase64: String? = null
)
