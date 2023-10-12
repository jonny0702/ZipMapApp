package com.example.zipmap


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.example.zipmap.ApiService.ApiClient
import com.example.zipmap.ApiService.ZipCodeDao
import com.example.zipmap.ApiService.ZipDto
import com.example.zipmap.helper.MapView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory





class InputZipCode : Fragment() {

    private var zipCode: String? = null
//    private  val responseObj: ZipCodeDao? = null
//    private lateinit var postalCode: ZipDto
    private lateinit var clickToSendValueFragment: ClickToSendValueFragment
    private lateinit var retrofit: Retrofit
    private var isClicked = false

    private var mapView: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            zipCode = it.getString(ZIP_CODE_BUNDLE)
        }
        retrofit = getRetrofitFrag()
    }
//    var zipInfo: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_input_zip_code, container, false)

        val btn: ImageButton = view.findViewById(R.id.send_button)
        val inputSearchLocation:EditText = view.findViewById(R.id.editText)

        var inputSearch = inputSearchLocation.addTextChangedListener {
            text -> zipCode = text.toString()
        }
        btn.setOnClickListener { v ->
            //TODO(): implement later
            when (v?.id) {
                R.id.send_button -> {
                    if(zipCode != null){
                        isClicked = true
                        searchLocationByZipCode(ZipDto(zipCode.orEmpty()))
                    }
                    Log.w("Zip_res: ", zipCode.toString())
                }

                else -> {

                }
            }
        }

        return view
    }

    private fun searchLocationByZipCode(postalCode: ZipDto){
        Log.i("postalCode", postalCode.toString())
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("zipCode", postalCode.toString())
            val responseApi = retrofit.create(ApiClient::class.java).getLocationZip(postalCode)

            if(responseApi.isSuccessful && responseApi.body() != null){
                Log.i("response.body", responseApi.body().toString())
                val markerOptions = MarkerOptions().position(
                    LatLng(
                    responseApi.body()!!.latitude, responseApi.body()!!.longitude
                )
                )
                mapView?.googleMap?.addMarker(markerOptions)
                clickToSendValueFragment.pushToSendToActivity(responseApi.body()!!, isClicked)
            }else{
                Log.i("response.body", "NotWorks")
            }
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            clickToSendValueFragment=context as ClickToSendValueFragment
            Log.i("isAttach", "works")
        }catch (e: Exception){
            Log.e("Exception Attach", e.message.toString())
        }
    }

    private fun getRetrofitFrag(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() 
    }



    companion object {
        private const val ZIP_CODE_BUNDLE = "507"

        @JvmStatic
        fun newInstance(zipCode: String) =
            InputZipCode().apply {
                arguments = Bundle().apply {
                    putString(ZIP_CODE_BUNDLE, zipCode)
                }
            }
    }


}

interface ClickToSendValueFragment{
    fun pushToSendToActivity(value: ZipCodeDao, isClicked: Boolean)

}