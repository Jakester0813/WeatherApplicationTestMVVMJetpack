package com.example.weatherapplicationtest.main

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplicationtest.data.LatLongObj
import com.example.weatherapplicationtest.data.WeatherResponse
import com.example.weatherapplicationtest.network.NetworkResult
import com.example.weatherapplicationtest.network.WeatherRepository
import com.example.weatherapplicationtest.ui.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel(){

    private var _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()
    var storedInput by mutableStateOf("")
    //This is
    var readyToNavigate = false
    val regex: Regex = Regex("^[a-zA-Z, ]*\$")

    var resultList: List<LatLongObj>? = null

    var currentLocation: Location? = null

    fun grabWeatherFromInput(input: String) {
        if (regex.containsMatchIn(input)) {
            viewModelScope.launch {
                _uiState.update {currentState ->
                    currentState.copy(
                        isInputEmpty = false,
                        isInputValid = true,
                        isLoading = true
                    )
                }
                when (val latLongResponse = repository.getLatLongFromInput(input)) {
                    is NetworkResult.Success -> {
                        resultList = latLongResponse.data
                        if (!resultList.isNullOrEmpty()) {
                            val firstResult = resultList!![0]
                            storedInput = input
                            getWeatherDataFromLatLong(firstResult.lat, firstResult.lon)
                        } else {
                            _uiState.update {currentState ->
                                currentState.copy(
                                    isInputEmpty = false,
                                    isLoading = false,
                                    errorMessage = "No results returned"
                                )
                            }
                        }

                    }
                    is NetworkResult.Error -> {
                        _uiState.update {currentState ->
                            currentState.copy(
                                isInputEmpty = false,
                                isLoading = false,
                                errorMessage = "There seems to be trouble fetching coordinates"
                            )
                        }
                    }
                }
            }
        }
        else {
            _uiState.update {currentState ->
                currentState.copy(
                    isInputEmpty = false,
                    isLoading = false,
                    errorMessage = "Seems you have invalid input"
                )
            }
        }
    }

    fun getWeatherFromLocationServices(lat: Double, long: Double){
        viewModelScope.launch {
            _uiState.update {currentState ->
                currentState.copy(
                    isInputEmpty = false,
                    isInputValid = true,
                    isLoading = true
                )
            }
            getWeatherDataFromLatLong(lat, long)
        }
    }

    private suspend fun getWeatherDataFromLatLong(lat: Double, long: Double) {
        when (val weatherResponse = repository.getWeather(lat, long)) {
            is NetworkResult.Success -> {
                val weatherData = weatherResponse.data?.body() as WeatherResponse
                readyToNavigate = true
                _uiState.update {currentState ->
                    currentState.copy(
                        isInputEmpty = false,
                        isInputValid = true,
                        isLoading = false,
                        data = weatherData
                    )
                }
            }
            is NetworkResult.Error -> {
                _uiState.update {currentState ->
                    currentState.copy(
                        isInputEmpty = false,
                        isLoading = false,
                        errorMessage = "There seems to be an error fetching the weather data"
                    )
                }
            }
        }
    }
}