package com.example.zipmap.ApiService

import com.google.gson.annotations.SerializedName

data class ZipCodeDao(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("city") val city: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("countryCode") val countryCode: String,

)
