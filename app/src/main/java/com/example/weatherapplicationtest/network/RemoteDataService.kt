package com.example.weatherapplicationtest.network

import com.example.weatherapplicationtest.data.LatLongObj
import com.example.weatherapplicationtest.data.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataService @Inject constructor(private val weatherService: WeatherService){
    suspend fun getLatLongFromInput(query: String) : List<LatLongObj>{
        return weatherService.getLatLongFromInput(query)
    }

    suspend fun getWeather(latitude: Double, longitude: Double) : Response<WeatherResponse> {
        return weatherService.getWeather(latitude, longitude)
    }
}