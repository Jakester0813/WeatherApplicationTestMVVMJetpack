package com.example.weatherapplicationtest.ui.composables

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.weatherapplicationtest.data.WeatherResponse
import com.example.weatherapplicationtest.parsers.MoshiParser

@Composable
fun WeatherScreen(navController: NavHostController, data: String){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Cyan)
            .padding(40.dp),
        //If I had more time, I would have set color based on time of day;
        //Cyan for day, Purple for nighttime
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val weatherData = MoshiParser().fromJson(data, WeatherResponse::class.java)
        val imageData = weatherData?.weather?.get(0)
        if (weatherData != null) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Today's Weather: ${imageData?.main}"
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = "Day: ${weatherData.main.temp_max.toInt()} * " +
                        "Night: ${weatherData.main.temp_min.toInt()}"
            )

            //For temp, feels like, weather icon, and description
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = "${weatherData.main.temp.toInt()}F",
                    fontSize = 48.sp
                )
                val context = LocalContext.current
                AsyncImage(
                    modifier = Modifier.size(100.dp),
                    model = ImageRequest.Builder(context)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .diskCacheKey("disk_cache")
                        .data("https://openweathermap.org/img/wn/${imageData?.icon}@2x.png")
                        .build(),
                    contentDescription = imageData?.description
                )
            }
            Text(text = "Feels like ${weatherData.main.feels_like.toInt()}F")
        }
        else {
            Toast.makeText(navController.context, "There is no data", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }

    }
    BackHandler {
        navController.popBackStack()
    }
}