package Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object APIService {

    val apiPeople: IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.Companion.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IAPIService::class.java)
    }

}