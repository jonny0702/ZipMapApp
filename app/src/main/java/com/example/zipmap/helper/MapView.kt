package com.example.zipmap.helper

import android.graphics.Color
import com.example.zipmap.MainActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapView(val activity: MainActivity, val googleMap: GoogleMap) {
    private val CAMERA_MARKER_COLOR: Int = Color.argb(255, 0, 255, 0)

    var setInitialCameraPosition = false
    var cameraIdle = true
    val cameraMarker = createMarker(CAMERA_MARKER_COLOR)

    init {
        googleMap.uiSettings.apply {
            isMapToolbarEnabled = true
            isIndoorLevelPickerEnabled = false
            isZoomControlsEnabled = true
            isTiltGesturesEnabled = false
            isScrollGesturesEnabled = true
        }
        googleMap.setOnMarkerClickListener { unused -> false }

        // Add listeners to keep track of when the GoogleMap camera is moving.
        googleMap.setOnCameraMoveListener { cameraIdle = false }
        googleMap.setOnCameraIdleListener { cameraIdle = true }
    }



    private fun createMarker(
        color: Int,

    ): Marker {
        val markersOptions = MarkerOptions()
            .position(LatLng(0.0,0.0))
            .draggable(false)
            .anchor(0.5f, 0.5f)
            .flat(true)
            .visible(false)
        return googleMap.addMarker(markersOptions)!!
    }



}