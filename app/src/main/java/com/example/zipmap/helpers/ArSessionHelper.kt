package com.example.zipmap.helpers

import android.app.Activity
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException

class ArSessionHelper(
    val activity: Activity,
    val features: Set<Session.Feature> = setOf()
): DefaultLifecycleObserver {
    var installRequested = false
    var session: Session? = null
        private  set

    var exceptionCallback: ((Exception) -> Unit)? = null

    var beforeSessionResume: ((Session) -> Unit)? = null
    private fun tryCreateSession(): Session?{
        // The app must have been given the CAMERA permission. If we don't have it yet, request it.
        if(!GeoPermissionsHelper.hasGeoPermissions(activity)){
            GeoPermissionsHelper.requestPermissions(activity)
            return null
        }

        return try {
            when(ArCoreApk.getInstance().requestInstall(activity, !installRequested)!!){
                ArCoreApk.InstallStatus.INSTALL_REQUESTED ->{
                    installRequested = true
                    return null}
                ArCoreApk.InstallStatus.INSTALLED -> {}
                }
                Session(activity, features)
            }catch(e: Exception) {
                exceptionCallback?.invoke(e)
                null
            }
        }

    override fun onResume(owner: LifecycleOwner) {
        Log.w("IS In Resume Life: ", "Yep")
        val session = this.session ?: tryCreateSession() ?: return
        try {
            beforeSessionResume?.invoke(session)
            session.resume()
            this.session = session
        }catch (e: CameraNotAvailableException){
            exceptionCallback?.invoke(e)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        session?.pause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        // Explicitly close the ARCore session to release native resources.
        session?.close()
        session = null
    }
    }

