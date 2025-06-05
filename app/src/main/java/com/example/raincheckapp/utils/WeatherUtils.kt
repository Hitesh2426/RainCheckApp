package com.example.raincheckapp.utils

import com.example.raincheckapp.data.model.ForecastItem

fun willItRain(forecastItem: ForecastItem): Boolean {
    return forecastItem.pop > 0.3 || // 30%+ rain probability
            forecastItem.weather.any { it.main in listOf("Rain", "Drizzle", "Thunderstorm") }
}