package model

data class ProductsGetResponse(val data: List<DTOProduct>,
                               val message: String,
                               val responseCode: Int)
