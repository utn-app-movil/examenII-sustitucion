package cr.ac.utn.appmovil.products.service

import cr.ac.utn.appmovil.products.model.GenericResponse
import cr.ac.utn.appmovil.products.model.AuthRequest
import cr.ac.utn.appmovil.products.model.Category
import cr.ac.utn.appmovil.products.model.Product
import cr.ac.utn.appmovil.products.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("users/auth")
    suspend fun login(@Body body: AuthRequest): Response<GenericResponse<Any>>

    @GET("users")
    suspend fun getUsers(): Response<GenericResponse<List<User>>>

    @GET("api/products")
    suspend fun getProducts(): Response<GenericResponse<List<Product>>>

    @POST("api/products")
    suspend fun createProduct(@Body product: Product): Response<GenericResponse<Product>>

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<GenericResponse<Product>>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<GenericResponse<Any>>

    @GET("api/categories")
    suspend fun getCategories(): Response<GenericResponse<List<Category>>>
}
