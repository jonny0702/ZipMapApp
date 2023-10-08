package com.example.zipmap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import androidx.fragment.app.add
import com.example.zipmap.helpers.GeoPermissionsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session

//AR

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    companion object{
        private const val LOCATION_REQUEST_CODE = 100
    }
    lateinit var arCoreSessionHelper: GeoPermissionsHelper
    lateinit var mapFragment: SupportMapFragment
    lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap

       mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!GeoPermissionsHelper.hasGeoPermissions(this)){
            Toast.makeText(this, "Camera and location permissions are needed to run this application", Toast.LENGTH_LONG)
                .show()
        }
        if(!GeoPermissionsHelper.shouldShowRequestPermissionRationale(this)){
            GeoPermissionsHelper.launchPermissionSettings(this)
        }
        finish()
    }

    fun createSession(session: Session){
        //Createing a new ArCore Session
        session.config.apply {
            // Enable Geospatial Mode
            geospatialMode = Config.GeospatialMode.ENABLED
        }
    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
            if(location != null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 20f))
            }
        }
    }
    private fun placeMarkerOnMap(currentLatLong: LatLng){
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = true

}