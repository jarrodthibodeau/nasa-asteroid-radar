package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val URL = BASE_URL + "neo/rest/v1/"

// There was a lot of socket timeout exceptions with the asteroids request, so I created a client that increases the timeout times for the request
private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(URL)
        .client(client.build())
        .build()

interface AsteroidApiService {
    @GET("feed")
    suspend fun getAsteroidFeed(@Query("api_key") apiKey: String, @Query("start_date") startDate: String?, @Query("end_date") endDate: String?): String
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}