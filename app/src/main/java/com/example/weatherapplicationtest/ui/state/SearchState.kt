package com.example.weatherapplicationtest.ui.state

import com.example.weatherapplicationtest.data.WeatherResponse

data class SearchState(
    val isInputEmpty: Boolean = true,
    val isInputValid: Boolean = true,
    val isLoading: Boolean = false,
    val data: WeatherResponse? = null,
    val errorMessage: String = ""
)