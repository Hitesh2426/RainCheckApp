package com.example.raincheckapp.data.network

import com.example.raincheckapp.data.models.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeocodingApiService {
    @Headers("User-Agent: RainCheckApp (hiteshgoyal843@gmail.com)")
    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json"
    ): GeocodingResponse
}