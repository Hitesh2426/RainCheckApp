package com.example.raincheckapp.data.models

import com.squareup.moshi.Json

data class GeocodingResponse(
    @Json(name = "display_name") val displayName: String?,
    val address: Address?
)

data class Address(
    val house_number: String?,
    val road: String?,
    val neighbourhood: String?,
    val city: String?,
    val town: String?,
    val village: String?,
    val state: String?,
    val postcode: String?,
    val country: String?,
    @Json(name = "country_code") val countryCode: String?
)