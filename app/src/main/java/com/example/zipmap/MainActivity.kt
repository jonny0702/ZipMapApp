package com.example.zipmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val LOCATION_REQUEST_CODE = 100
    }

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var inputFragment: InputZipCode
    val fragmentTransaction = supportFragmentManager.beginTransaction()

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

}