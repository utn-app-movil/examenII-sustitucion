package cr.ac.utn.appmovil.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cr.ac.utn.appmovil.products.model.Category
import cr.ac.utn.appmovil.products.model.Product
import cr.ac.utn.appmovil.products.repository.ProductRepository
import cr.ac.utn.appmovil.products.service.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ProductViewModel : BaseViewModel() {
    private val repository = ProductRepository(RetrofitClient.instance)

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    fun loadProducts() {
        viewModelScope.launch {
            try {
                handleResponse(repository.getProducts()) { response ->
                    _products.postValue(response.data ?: emptyList())
                }
            } catch (e: Exception) {
            }
        }
    }
    
    fun loadCategories() {
         viewModelScope.launch {
            handleResponse(repository.getCategories()) { response ->
                _categories.postValue(response.data ?: emptyList())
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            handleResponse(repository.deleteProduct(id)) {
                loadProducts()
            }
        }
    }
    
    fun createProduct(product: Product) {
        viewModelScope.launch {
            handleResponse(repository.createProduct(product)) {
                loadProducts()
            }
        }
    }

    fun updateProduct(id: Int, product: Product) {
        viewModelScope.launch {
            handleResponse(repository.updateProduct(id, product)) {
                loadProducts()
            }
        }
    }
    private var pollingJob: kotlinx.coroutines.Job? = null

    fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (isActive) {
                loadProducts()
                delay(4000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}
