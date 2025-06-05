package com.example.raincheckapp.data.network

import com.example.raincheckapp.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    // OkHttp client with User-Agent header for Nominatim API
    private val geocodingHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "RainCheckApp (hiteshgoyal843@gmail.com)")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    val geocodingApiService: GeocodingApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_GEOCODING_URL)
            .client(geocodingHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApiService::class.java)
    }

    val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}