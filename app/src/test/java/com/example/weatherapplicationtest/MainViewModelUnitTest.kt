package com.example.weatherapplicationtest

import android.location.Location
import com.example.weatherapplicationtest.data.LatLongObj
import com.example.weatherapplicationtest.data.Main
import com.example.weatherapplicationtest.data.WeatherObj
import com.example.weatherapplicationtest.data.WeatherResponse
import com.example.weatherapplicationtest.main.MainViewModel
import com.example.weatherapplicationtest.network.NetworkResult
import com.example.weatherapplicationtest.network.WeatherRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class MainViewModelUnitTest {

    @MockK
    private lateinit var repository: WeatherRepository
    @MockK
    private lateinit var viewModel: MainViewModel

    private val lat = 37.29280384615605
    private val long = -121.91302913767753
    val response = WeatherResponse(
        weather = listOf(WeatherObj(main = "Sunny", icon = "10D", description = "cool sunny day")),
        main = Main(temp = 70.0, feels_like = 60.0, temp_min = 55.0, temp_max = 75.0)
    )
    var locationObj = Location("location")

    @BeforeEach
    fun before(){
        repository = mockk(relaxed = true)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MainViewModel(repository)
        locationObj.latitude = lat
        locationObj.longitude = long
        coEvery {
            repository.getLatLongFromInput(query = any())
        } returns NetworkResult.Success(
            data = listOf(LatLongObj(lat = lat, lon = long))
        )

        coEvery {
            repository.getWeather(latitude = any(), longitude = any())
        } returns NetworkResult.Success(data = Response.success(response))

    }

    @Test
    @DisplayName("Search Input does not match regex")
    fun inputNotMatchingRegex(){
        viewModel.grabWeatherFromInput("!$#*(&")
        viewModel.uiState.value.errorMessage shouldBeEqualTo "Seems you have invalid input"
        viewModel.uiState.value.isInputEmpty shouldBeEqualTo false
        viewModel.uiState.value.isLoading shouldBeEqualTo false

    }

    //ToDo: Make this test work before submitting
    @Test
    @DisplayName("Search Input manages to get coordinates and then weather data")
    fun successfulSearchAndWeatherRetrieval(){
        val input = "San Jose, CA, USA"
        coEvery { repository.getLatLongFromInput(input) } returns
                NetworkResult.Success(listOf(LatLongObj(lat, long)))
        coEvery { repository.getWeather(lat, long) }returns
                NetworkResult.Success(Response.success(response))
        viewModel.grabWeatherFromInput(input)
        viewModel.uiState.value.data shouldBeEqualTo  response
        viewModel.readyToNavigate shouldBeEqualTo true
        viewModel.uiState.value.isInputEmpty shouldBeEqualTo false
        viewModel.uiState.value.isInputValid shouldBeEqualTo true
        viewModel.uiState.value.isLoading shouldBeEqualTo false
    }

    @Test
    @DisplayName("Search Input fails to get coordinates")
    fun getLatLongReturnsFailure(){
        val input = "San Jose, CA, USA"
        coEvery { repository.getLatLongFromInput(input) } returns
                NetworkResult.Error("")
        viewModel.grabWeatherFromInput(input)
        viewModel.uiState.value.errorMessage shouldBeEqualTo
                "There seems to be trouble fetching coordinates"
        viewModel.uiState.value.isInputEmpty shouldBeEqualTo false
        viewModel.uiState.value.isLoading shouldBeEqualTo false
    }

    @Test
    @DisplayName("Search Input manages to get empty list")
    fun getLatLongReturnsEmptyList(){
        val input = "San Jose, CA, USA"
        coEvery { repository.getLatLongFromInput(input) } returns
                NetworkResult.Success(emptyList())
        coEvery { repository.getWeather(lat, long) }returns
                NetworkResult.Success(Response.success(response))
        viewModel.grabWeatherFromInput(input)
        viewModel.uiState.value.errorMessage shouldBeEqualTo "No results returned"
        viewModel.uiState.value.isInputEmpty shouldBeEqualTo false
        viewModel.uiState.value.isLoading shouldBeEqualTo false
    }

    @Test
    @DisplayName("Grab Weather Data returns failure")
    fun grabWeatherDataFailure(){
        coEvery { repository.getWeather(lat, long) }returns
                NetworkResult.Error("")
        viewModel.getWeatherFromLocationServices(lat, long)
        viewModel.uiState.value.errorMessage shouldBeEqualTo
                "There seems to be an error fetching the weather data"
        viewModel.uiState.value.isInputEmpty shouldBeEqualTo false
        viewModel.uiState.value.isLoading shouldBeEqualTo false
    }
}