package cr.ac.utn.appmovil.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cr.ac.utn.appmovil.products.model.AuthRequest
import cr.ac.utn.appmovil.products.model.GenericResponse
import cr.ac.utn.appmovil.products.model.User
import cr.ac.utn.appmovil.products.repository.ProductRepository
import cr.ac.utn.appmovil.products.service.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    private val repository = ProductRepository(RetrofitClient.instance)

    private val _loginResult = MutableLiveData<GenericResponse<Any>>()
    val loginResult: LiveData<GenericResponse<Any>> = _loginResult

    private val _usersResult = MutableLiveData<List<User>>()
    val usersResult: LiveData<List<User>> = _usersResult

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            try {
                handleResponse(repository.login(AuthRequest(username, pass))) { successfulResponse ->
                    _loginResult.postValue(successfulResponse)
                }
            } catch (e: Exception) {
                _statusMessage.postValue(GenericResponse(null, "INTERNAL_ERROR", e.message ?: "Unknown error"))
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            try {
                handleResponse(repository.getUsers()) { response ->
                    _usersResult.postValue(response.data ?: emptyList())
                }
            } catch (e: Exception) {
                 _statusMessage.postValue(GenericResponse(null, "INTERNAL_ERROR", e.message ?: "Unknown error"))
            }
        }
    }
}
