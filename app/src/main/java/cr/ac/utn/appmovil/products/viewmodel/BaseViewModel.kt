package cr.ac.utn.appmovil.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cr.ac.utn.appmovil.products.model.GenericResponse
import retrofit2.Response

abstract class BaseViewModel : ViewModel() {

    protected val _statusMessage = MutableLiveData<GenericResponse<*>>()
    val statusMessage: LiveData<GenericResponse<*>> = _statusMessage

    protected fun <T> handleResponse(response: Response<GenericResponse<T>>, onSuccess: (GenericResponse<T>) -> Unit) {
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            _statusMessage.postValue(body)
            onSuccess(body)
        } else {
            val gson = Gson()
            val type = object : TypeToken<GenericResponse<T>>() {}.type
            var errorResponse: GenericResponse<T>? = null
            
            try {
                if (response.errorBody() != null) {
                    errorResponse = gson.fromJson(response.errorBody()!!.charStream(), type)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (errorResponse != null) {
                _statusMessage.postValue(errorResponse)
            } else {
                _statusMessage.postValue(GenericResponse<T>(null, "INTERNAL_ERROR", "Error: ${response.code()} ${response.message()}"))
            }
        }
    }
}
