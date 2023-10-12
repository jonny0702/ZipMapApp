package com.example.zipmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import com.example.zipmap.ApiService.ZipCodeDao
import com.example.zipmap.helper.MapView
import com.example.zipmap.helper.MapViewHelper

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import retrofit2.Retrofit

class MainActivity() : AppCompatActivity(), ClickToSendValueFragment, GoogleMap.OnMarkerClickListener {

    private lateinit var retrofit: Retrofit

    lateinit var view: MapViewHelper

    private lateinit var inputFragment: InputZipCode
    private var inputValueFragment: ZipCodeDao? = null

     var lastLocation: Location? = null
     lateinit var fusedLocationProviderClient: FusedLocationProviderClient


//    private var locationPermissionGranted = false
//    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(9.04, -79.48)

    //State Listener
    private var clickedStateButton = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        view = MapViewHelper(this, fusedLocationProviderClient, lastLocation)
        lifecycle.addObserver(view)

        (view.root)

        supportFragmentManager.commit {
            add<InputZipCode>(R.id.input_fragment_container)
        }

    }


    override fun pushToSendToActivity (value: ZipCodeDao, isClicked: Boolean){

        clickedStateButton = isClicked
        Log.i("click Status", clickedStateButton.toString())

        inputValueFragment = value
        Log.i("FragmentInputValue", inputValueFragment.toString())
    }



    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val LOCATION_REQUEST_CODE = 1
        const val BASE_URL = "http://192.168.0.13:8000/"
        private const val DEFAULT_ZOOM = 15f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    }


    override fun onMarkerClick(p0: Marker) = false


}