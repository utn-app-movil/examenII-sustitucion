package cr.ac.utn.appmovil.products.Service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import cr.ac.utn.appmovil.products.util.util
import java.util.concurrent.TimeUnit

object APIService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiPeople: IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.apiURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IAPIService::class.java)
    }
}