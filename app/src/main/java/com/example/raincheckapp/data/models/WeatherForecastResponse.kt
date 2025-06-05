package com.example.raincheckapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("list") val forecasts: List<ForecastItem>,
    @SerializedName("city") val city: City
)

data class ForecastItem(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("dt_txt") val dateTime: String,
    @SerializedName("main") val main: MainData,
    @SerializedName("weather") val weather: List<WeatherDescription>,
    @SerializedName("rain") val rain: RainData?,
    @SerializedName("pop") val pop: Float = 0f
)

data class MainData(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

data class RainData(
    @SerializedName("3h") val volume: Double?
)

data class City(
    val name: String
)