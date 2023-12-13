package com.example.weatherapplicationtest.network


//If I had more time, I would have worked on this to specify the errors so I can handle
//different errors better
sealed class NetworkResult<T> (val data:T? = null, val error:String? = null) {
    class Success<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(e: String, data: T? = null) : NetworkResult<T> (data, e)
}