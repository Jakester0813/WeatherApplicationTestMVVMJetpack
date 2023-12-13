package com.example.weatherapplicationtest.network

import javax.inject.Inject

class WeatherRepository @Inject constructor(private val remoteDataService: RemoteDataService)
    : BaseAPIResponse() {
    suspend fun getLatLongFromInput(query: String) = safeApiCall {
        remoteDataService.getLatLongFromInput(query)
    }

    suspend fun getWeather(latitude: Double, longitude: Double) = safeApiCall {
        remoteDataService.getWeather(latitude, longitude)
    }
}