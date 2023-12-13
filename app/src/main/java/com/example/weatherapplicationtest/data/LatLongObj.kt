package com.example.weatherapplicationtest.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatLongObj (
    @field:Json(name ="lat") val lat: Double,
    @field:Json(name = "lon") val lon: Double
)