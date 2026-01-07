package model

import com.google.gson.annotations.SerializedName

data class DTOProduct(@SerializedName("id") val Id: Int,
                      @SerializedName("nombre") val Nombre: String="",
                      @SerializedName("categoria") val Categoria: String="",
                      @SerializedName("precio") val Precio: Float=0.0f,
                      @SerializedName("cantidad") val Cantidad: Int=0,
                      @SerializedName("fotoPath") val FotoPath: String="")