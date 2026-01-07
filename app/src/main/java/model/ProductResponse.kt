package model

data class ProductResponse(val data: DTOProduct?,
                           val message: String,
                           val responseCode: Int)
