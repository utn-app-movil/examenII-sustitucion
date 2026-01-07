package model

import com.google.gson.annotations.SerializedName

data class DTOUser(@SerializedName("user") val User: String,
                   @SerializedName("name") val Name: String="",
                   @SerializedName("lastname") val Lastname: String="",
                   @SerializedName("email") val Email: String="")
