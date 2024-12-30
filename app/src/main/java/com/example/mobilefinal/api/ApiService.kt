package com.example.mobilefinal.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

object RetrofitInstance {
    private const val BASE_URL = "https://detect.roboflow.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

sealed class ApiState<T> {
    data class Success<T>(val data: T? = null) : ApiState<T>()
    data class Error<T>(val message: String? = null) : ApiState<T>()
    class Loading<T> : ApiState<T>()
    class Empty<T> : ApiState<T>()
}


interface ApiService {
    @POST("bottles-and-cups-detection-6u8tg/24")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun detectObject(
        @Query("api_key") apiKey: String = "2E7l6LQpywlBKwN9qTQ4",
        @Body base64Image: String
    ): DetectionResponse
}