package com.example.zipmap.client.network

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.io.copyTo

import javax.inject.Inject
class ZipMapHttpClient {
    companion object
    {
        private var instance: ZipMapHttpClient? = null
        fun createInstance (context: Context): ZipMapHttpClient
        {
            if (instance == null)
            {
                instance = ZipMapHttpClient()
                instance!!.initialize(context)
            }
            return instance!!
        }

    }

    private var client: HttpClient? = null
    fun initialize (context: Context)
    {
        client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            install(Logging){
                logger = object : Logger{
                    override fun log(message: String) {
                        Log.i("KTOR_LOGGER", message)
                    }
                }
            }
            install(ResponseObserver){
                onResponse { response -> Log.i("ktor_logger", "${response.status.value }") }
            }
        }
        suspend fun sendPostRequest (zipCodeNumber: String): String? = coroutineScope{
           try {
                val response = client!!.post{
                    url("http://192.168.0.13:8000/destination/postal_code/")
                    setBody(FormDataContent(Parameters.build {
                        append("postalCode", zipCodeNumber)
                    }))
                }
                Log.i("Post_Request",  "The data has sended")
                response.body()
            }catch (e: Exception){
                Log.d("DEBUG DATA", "HTTPS EXCEPTIONS: ${e.toString()}")
                 null
            }

        }

    }


    /** companion object{
        private const val TIME_OUT = 10_000
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
    } **/

}