package com.example.raincheckapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.example.raincheckapp.data.network.NetworkModule
import com.example.raincheckapp.data.repository.WeatherRepository
import com.example.raincheckapp.ui.view.MainScreen
import com.example.raincheckapp.ui.theme.RainCheckAppTheme
import com.example.raincheckapp.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = WeatherRepository(
            geocodingApiService = NetworkModule.geocodingApiService,
            weatherApiService = NetworkModule.weatherApiService
        )
        setContent {
            RainCheckAppTheme {
                val viewModel = remember { MainViewModel(repository) }
                MainScreen(viewModel = viewModel)
            }
        }
    }
}