package com.example.weatherapplicationtest.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherObj (
    @field:Json(name ="main") val main: String,
    @field:Json(name ="description") val description: String,
    @field:Json(name ="icon") val icon: String
) : Parcelable