package Service

import model.CategoriesGetResponse
import model.DTOLogin
import model.DTOProduct
import model.DTOUser
import model.LoginRequest
import model.ProductResponse
import model.ProductsGetResponse
import model.UserResponse
import model.UsersGetResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IAPIService {

    //PRODUCTOS
    @GET("/api/products")
    suspend fun getProducts(): ProductsGetResponse

    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductsGetResponse

    @Headers("Content-type: application/json")
    @POST("/api/products")
    suspend fun postProduct(@Body product: DTOProduct): ProductResponse

    @Headers("Content-type: application/json")
    @PUT("/api/products")
    suspend fun updateProduct(@Body product: DTOProduct): ProductResponse

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "/api/products", hasBody = true)
    suspend fun deleteProduct(@Body product: DTOProduct): ProductResponse

    //CATEGORIAS
    @GET("/api/categories")
    suspend fun getCategories(): CategoriesGetResponse

    //USUARIOS
    @Headers("Content-Type: application/json")
    @POST("/users/auth")
    suspend fun authUser(@Body request: DTOLogin): UserResponse


}