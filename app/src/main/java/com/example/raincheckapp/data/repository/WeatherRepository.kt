package com.example.raincheckapp.data.repository

import android.util.Log
import com.example.raincheckapp.data.network.GeocodingApiService
import com.example.raincheckapp.data.network.WeatherApiService
import com.example.raincheckapp.domain.model.WeatherInfo
import com.example.raincheckapp.utils.Constants
import com.example.raincheckapp.utils.willItRain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class WeatherRepository(
    private val geocodingApiService: GeocodingApiService,
    private val weatherApiService: WeatherApiService
) {

    suspend fun getCityName(lat: Double, lon: Double): String = withContext(Dispatchers.IO) {
        try {
            val response = geocodingApiService.reverseGeocode(lat, lon)
            val address = response.address
            Log.e("RESPONSE:","$response")
            val location = address?.city ?: address?.town ?: address?.village ?: "Unknown Location"
            println("LOCATION: $location")
            location // ✅ Return the value
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown Location"
        }
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double, selectedDate: String): WeatherInfo? = withContext(Dispatchers.IO) {
        try {
            val response = weatherApiService.getWeatherForecast(lat, lon, Constants.WEATHER_API_KEY)

            println("DEBUG: Total forecast items received: ${response.forecasts.size}")


            val matchingItem = response.forecasts.firstOrNull { it.dateTime.startsWith(selectedDate) }

            matchingItem?.let {
                val weatherDesc = it.weather.firstOrNull()
                val rainExpected = willItRain(it)
                WeatherInfo(
                    temperature = it.main.temp,
                    feelsLike = it.main.feelsLike,
                    humidity = it.main.humidity,
                    rainVolume = it.rain?.volume ?: 0.0,
                    weatherMain = weatherDesc?.main ?: "N/A",
                    weatherDescription = weatherDesc?.description ?: "N/A",
                    iconUrl = weatherDesc?.icon?.let { icon -> "https://openweathermap.org/img/wn/${icon}@2x.png" } ?: "",
                    dateTime = it.dateTime,
                    willItRain = rainExpected
                )
            }
        } catch (e: Exception) {
            println("Error fetching forecast: ${e.message}")
            null
        }
    }
}