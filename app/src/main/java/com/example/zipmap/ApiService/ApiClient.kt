package com.example.zipmap.ApiService

import android.text.Editable
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {
    @POST("destination/postal_code/")
    suspend fun getLocationZip(@Body zipDtop: ZipDto):Response<ZipCodeDao>
}

data class ZipDto(@SerializedName("postalCode") val postalCode: String)