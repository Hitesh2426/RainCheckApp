package com.example.raincheckapp.domain.model

data class WeatherInfo(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val rainVolume: Double,
    val weatherMain: String,
    val weatherDescription: String,
    val iconUrl: String,
    val dateTime: String,
    val willItRain: Boolean
)