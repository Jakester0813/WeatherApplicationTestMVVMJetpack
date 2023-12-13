package com.example.weatherapplicationtest.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseAPIResponse {
    suspend inline fun <T> safeApiCall(
        crossinline body: suspend () -> T
    ): NetworkResult<T> {
        return try {
            val response = withContext(Dispatchers.IO) { body() }
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}