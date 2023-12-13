package com.example.weatherapplicationtest.data

import com.squareup.moshi.Json

data class Coordinates (
    @field:Json(name ="lon") val longitude: Double,
    @field:Json(name = "lat") val latitude: Double
)