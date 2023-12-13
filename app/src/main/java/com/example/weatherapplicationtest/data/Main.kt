package com.example.weatherapplicationtest.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Main (
    @field:Json(name = "temp") val temp: Double,
    @field:Json(name = "feels_like") val feels_like: Double,
    @field:Json(name ="temp_min") val temp_min: Double,
    @field:Json(name = "temp_max") val temp_max: Double
) : Parcelable