package model

data class CategoriesGetResponse(val data: List<DTOCategories>,
                                 val message: String,
                                 val responseCode: Int)
