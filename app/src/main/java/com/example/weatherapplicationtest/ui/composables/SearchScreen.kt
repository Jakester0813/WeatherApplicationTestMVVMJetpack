package com.example.weatherapplicationtest.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.weatherapplicationtest.R
import com.example.weatherapplicationtest.data.WeatherResponse
import com.example.weatherapplicationtest.parsers.MoshiParser
import com.example.weatherapplicationtest.main.MainViewModel

@Composable
fun SearchScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController
){
    val uiState by viewModel.uiState.collectAsState()
    var input by remember { mutableStateOf(viewModel.storedInput) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.search_text),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        TextField(
            modifier = Modifier.padding(16.dp),
            value = input,
            onValueChange = {input = it},
            maxLines = 1
            )
        if (!uiState.isLoading) {
            if(!uiState.errorMessage.isNullOrEmpty()) {
                Text(
                    text = uiState.errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            Button(onClick = { viewModel.grabWeatherFromInput(input) }) {
                Text(text = "Search")
            }
        }
        else {
            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Loading data...please wait")
                Spacer(modifier = Modifier.padding(start=4.dp, end = 4.dp))
                CircularProgressIndicator(modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically))
            }
        }
    }

    //viewModel.readyToNavigate was used to fix a bug where if it goes back to the previous screen,
    //it would have gone straight back to the next screen. If I had more time, I would have handled
    //this more cleanly
    if (!uiState.isLoading && uiState.data != null && viewModel.readyToNavigate) {
        viewModel.readyToNavigate = false
        navController.navigate("weather_data_screen/weatherData={weatherData}"
            .replace("{weatherData}",
                MoshiParser().toJson(uiState.data!!, WeatherResponse::class.java)!!))
    }
}

