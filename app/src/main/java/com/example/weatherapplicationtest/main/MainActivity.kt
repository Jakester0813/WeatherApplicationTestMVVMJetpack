package com.example.weatherapplicationtest.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.weatherapplicationtest.ui.composables.NavGraph
import com.example.weatherapplicationtest.ui.theme.WeatherApplicationTestTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    //If I had more time, I would have worked on implementing DataStore for storing location
    private lateinit var sharedPref: SharedPreferences
    val mainViewModel: MainViewModel by viewModels()
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        100
    ).build()
    private lateinit var locationManger: LocationManager

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val locationList = result.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.first()
                mainViewModel.getWeatherFromLocationServices(location.latitude, location.longitude)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("data", Context.MODE_PRIVATE)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationManger = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        askLocationPermission()
        setContent {
            WeatherApplicationTestTheme {
                NavGraph(viewModel = mainViewModel, navController = rememberNavController())
            }
        }
    }

    private fun askLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProvider?.requestLocationUpdates(locationRequest, locationCallback,
                    Looper.getMainLooper())
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.storedInput = sharedPref.getString("input", "").toString()
    }

    override fun onPause() {
        super.onPause()
        if (mainViewModel.storedInput.isNotEmpty()) {
            val editor = sharedPref.edit()
            editor.putString("input", mainViewModel.storedInput)
            editor.apply()
        }
    }
}
