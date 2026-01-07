package cr.ac.utn.appmovil.products.Service

import cr.ac.utn.appmovil.products.model.AuthRequest
import cr.ac.utn.appmovil.products.model.AuthResponse
import cr.ac.utn.appmovil.products.model.CategoriesResponse
import cr.ac.utn.appmovil.products.model.ProductRequest
import cr.ac.utn.appmovil.products.model.ProductResponse
import cr.ac.utn.appmovil.products.model.ProductsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IAPIService {

    // ============================================
    // AUTHENTICATION ENDPOINTS
    // ============================================

    /**
     * Authenticate user
     * POST /users/auth
     */
    @POST("users/auth")
    fun authenticateUser(@Body authRequest: AuthRequest): Call<AuthResponse>

    /**
     * Get all users (for reference)
     * GET /users
     */
    @GET("users")
    fun getAllUsers(): Call<AuthResponse>


    // ============================================
    // PRODUCTS ENDPOINTS
    // ============================================

    /**
     * Get all products
     * GET /api/products
     */
    @GET("api/products")
    fun getAllProducts(): Call<ProductsResponse>

    /**
     * Get product by ID
     * GET /api/products/{id}
     */
    @GET("api/products/{id}")
    fun getProductById(@Path("id") id: Int): Call<ProductResponse>

    /**
     * Create new product
     * POST /api/products
     */
    @POST("api/products")
    fun createProduct(@Body product: ProductRequest): Call<ProductResponse>

    /**
     * Update existing product
     * PUT /api/products/{id}
     */
    @PUT("api/products/{id}")
    fun updateProduct(@Path("id") id: Int, @Body product: ProductRequest): Call<ProductResponse>

    /**
     * Delete product
     * DELETE /api/products/{id}
     */
    @DELETE("api/products/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<ProductResponse>

    /**
     * Get all categories
     * GET /api/categories
     */
    @GET("api/categories")
    fun getAllCategories(): Call<CategoriesResponse>
}