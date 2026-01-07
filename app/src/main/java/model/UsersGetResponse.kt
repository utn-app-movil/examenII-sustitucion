package model

data class UsersGetResponse(val data: List<DTOUser>,
                            val message: String,
                            val responseCode: Int)
