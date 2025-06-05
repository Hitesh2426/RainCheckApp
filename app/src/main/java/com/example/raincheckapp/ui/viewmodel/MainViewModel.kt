package com.example.raincheckapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raincheckapp.data.repository.WeatherRepository
import com.example.raincheckapp.domain.model.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    data class Success(
        val cityName: String,
        val weatherInfo: WeatherInfo,
        val lastUpdated: String,
        val selectedDate: String
    ) : UiState
    data class Error(val message: String) : UiState
}

class MainViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var currentLat: Double? = null
    private var currentLon: Double? = null

    private var selectedDate: String = ""

    fun setLocation(lat: Double, lon: Double) {
        currentLat = lat
        currentLon = lon
        refreshWeather()
    }

    fun selectDate(date: String) {
        selectedDate = date
        refreshWeather()
    }

    fun refreshWeather() {
        val lat = currentLat
        val lon = currentLon

        if (lat == null || lon == null) {
            _uiState.value = UiState.Error("Location not set")
            return
        }

        if (selectedDate.isBlank()) {
            _uiState.value = UiState.Error("Date not selected")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val cityName = repository.getCityName(lat, lon)
                val weather = repository.getWeatherForecast(lat, lon, selectedDate)

                if (weather != null) {
                    _uiState.value = UiState.Success(
                        cityName = cityName,
                        weatherInfo = weather,
                        lastUpdated = weather.dateTime,
                        selectedDate = selectedDate
                    )
                } else {
                    _uiState.value = UiState.Error("Weather data unavailable")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}