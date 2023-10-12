package com.example.zipmap.helper

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import com.example.zipmap.R
import com.example.zipmap.MainActivity
import com.example.zipmap.helper.MapView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapViewHelper(val activity: MainActivity, fusedLocation: FusedLocationProviderClient, lastLocation: Location?): DefaultLifecycleObserver {

    val root  = View.inflate(activity,  R.layout.activity_main, null)
    var mapView: MapView? = null
    var googleMapObj: GoogleMap? = null
    var lastLoc: Location? = lastLocation

    val mapFragment =
        (activity.supportFragmentManager.findFragmentById(R.id.mapFragment)!! as SupportMapFragment).also {
            it.getMapAsync { googleMap ->
                mapView = MapView(activity, googleMap)
                googleMapObj = googleMap
                settingPermitions(googleMap, fusedLocation)
            }
        }

    private fun settingPermitions(googleMap: GoogleMap?, fusedLocation: FusedLocationProviderClient){
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.LOCATION_REQUEST_CODE
            )
            return
        }
        googleMap?.isMyLocationEnabled = true


        fusedLocation.lastLocation.addOnSuccessListener(activity) { location ->

            if(location != null){
                lastLoc = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 20f))
            }
        }
    }
}
