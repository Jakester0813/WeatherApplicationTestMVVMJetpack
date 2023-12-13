package com.example.weatherapplicationtest.network

import com.example.weatherapplicationtest.BuildConfig
import com.example.weatherapplicationtest.data.LatLongObj
import com.example.weatherapplicationtest.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("geo/1.0/direct")
    suspend fun getLatLongFromInput(
        @Query("q")query: String,
        @Query("appId") apiKey: String = BuildConfig.API_KEY
    ) : List<LatLongObj>

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat")lat: Double,
        @Query("lon") lon: Double,
        @Query("appId") apiKey: String = BuildConfig.API_KEY,
        @Query("units") units: String = "imperial"
    ) : Response<WeatherResponse>
}