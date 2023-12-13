package com.example.weatherapplicationtest.parsers

interface JsonParser {
    fun <T> fromJson(json: String, type: Class<T>): T?
    fun <T> toJson(obj: T, type: Class<T>): String?
}