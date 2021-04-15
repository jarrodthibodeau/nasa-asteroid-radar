package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val URL = BASE_URL +  "planetary/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(URL)
        .build()

interface PictureOfTheDayApiService {
    @GET("apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String, @Query("date") date: String): PictureOfDay
}

object PictureOfTheDayApi {
    val retrofitService: PictureOfTheDayApiService by lazy {
        retrofit.create(PictureOfTheDayApiService::class.java)
    }
}