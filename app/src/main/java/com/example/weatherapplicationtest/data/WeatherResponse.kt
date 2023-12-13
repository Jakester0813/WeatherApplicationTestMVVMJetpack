package com.example.weatherapplicationtest.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WeatherResponse (
    @field:Json(name = "weather") val weather: List<WeatherObj>,
    @field:Json(name = "main") val main: Main
) : Parcelable