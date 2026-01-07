package cr.ac.utn.appmovil.products.repository

import cr.ac.utn.appmovil.products.model.AuthRequest
import cr.ac.utn.appmovil.products.model.Product
import cr.ac.utn.appmovil.products.service.ApiService

class ProductRepository(private val apiService: ApiService) {

    suspend fun login(authRequest: AuthRequest) = apiService.login(authRequest)
    suspend fun getUsers() = apiService.getUsers()
    suspend fun getProducts() = apiService.getProducts()
    suspend fun createProduct(product: Product) = apiService.createProduct(product)
    suspend fun updateProduct(id: Int, product: Product) = apiService.updateProduct(id, product)
    suspend fun deleteProduct(id: Int) = apiService.deleteProduct(id)
    suspend fun getCategories() = apiService.getCategories()
}
