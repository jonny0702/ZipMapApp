package com.example.zipmap

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import com.example.zipmap.ApiService.ApiClient
import com.example.zipmap.ApiService.HeaderInterceptor
import com.example.zipmap.ApiService.ZipDto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback, ClickToSendValueFragment {

    companion object {
        private const val LOCATION_REQUEST_CODE = 100
        const val BASE_URL = "http://192.168.0.13:8000/"
    }
    private lateinit var retrofit: Retrofit

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    private lateinit var inputFragment: InputZipCode
    private var inputValueFragment: String = ""

    //    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment =
        supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        supportFragmentManager.commit {
            add<InputZipCode>(R.id.input_fragment_container)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        mMap.uiSettings.isZoomControlsEnabled = false
//        mMap.setOnMarkerClickListener(this)
//        setUpMap()

        val panama = LatLng(9.04, -79.48)
        val markerText: String = getString(R.string.marker_title)

        mMap.addMarker(
            MarkerOptions().position(panama).title(markerText)
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(panama))
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
//        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
//            if (location != null) {
//                lastLocation = location
//                val currentLatLong = LatLng(location.latitude, location.longitude)
//                placeMarkerOnMap(currentLatLong)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 20f))
//            }
//        }
    }
    

//    private fun placeMarkerOnMap(currentLatLong: LatLng) {
//        val markerOptions = MarkerOptions().position(currentLatLong)
//        markerOptions.title("$currentLatLong")
//        mMap.addMarker(markerOptions)
//    }

//    override fun onMarkerClick(p0: Marker) = true
    override fun pushToSendToActivity (value: String){
        inputValueFragment = value
        Log.i("FragmentInputValue", inputValueFragment)
    }
  /**  private fun searchLocationByZipCode(postalCode: String){
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("zipCode", postalCode)
            val responseApi = retrofit.create(ApiClient::class.java).getLocationZip(ZipDto(postalCode))
            if(responseApi.isSuccessful && responseApi.body() != null){
                Log.i("response.body", responseApi.body().toString())
            }
        }

    }**/

/**   private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
                .build()
    }**/
    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()
    }
}