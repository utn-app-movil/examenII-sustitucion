package cr.ac.utn.appmovil.products.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Use Cloud URL by default as it is the stable production endpoint
    private const val BASE_URL_CLOUD = "https://apiproduct-d4aefnf0h9gscvc4.eastus-01.azurewebsites.net/"
    private const val BASE_URL_EMULATOR = "http://10.0.2.2:3000/"
    
    // Switch this variable to change environments (or implement dynamic checking if needed)
    private const val CURRENT_URL = BASE_URL_CLOUD

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(CURRENT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
