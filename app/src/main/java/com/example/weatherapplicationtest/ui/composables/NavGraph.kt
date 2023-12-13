package com.example.weatherapplicationtest.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapplicationtest.main.MainViewModel

@Composable
fun NavGraph (
    viewModel: MainViewModel,
    navController: NavHostController
) {
    NavHost(navController = navController,
        startDestination = "search_screen") {
        composable("search_screen") {
            SearchScreen(viewModel, navController)
        }
        composable(
            "weather_data_screen/weatherData={weatherData}"
        ) {
            it.arguments?.getString("weatherData")?.let { data ->
                WeatherScreen(navController, data)
            }
        }
    }
}