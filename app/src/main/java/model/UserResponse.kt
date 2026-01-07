package model

data class UserResponse(val data: DTOUser?,
                        val message: String,
                        val responseCode: String)
