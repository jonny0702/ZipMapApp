package com.example.zipmap

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.example.zipmap.client.network.ZipMapHttpClient


class InputZipCode : Fragment(), View.OnClickListener {

    private var zipCode: String? = null
    lateinit var handleClick: handleClickFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            zipCode = it.getString(ZIP_CODE_BUNDLE)
        }

    }
    var zipInfo: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_zip_code, container, false)
        val btn: ImageButton = view.findViewById(R.id.send_button)
        val zipInputCode: EditText = view.findViewById(R.id.editText_zip)


        Log.w("object onCreateView: ", this.toString())
        Log.w("Input_id: ", zipInputCode.toString())
        var zipCodeRep = zipInputCode.addTextChangedListener {
            text -> zipInfo = text.toString()
        }
        Log.w("ZipInfo: ", zipCodeRep.toString())
        btn.setOnClickListener(this)
        return view
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


    override fun onClick(v: View?){
        when(v?.id){
            R.id.send_button -> {
                handleClick.pushOnSendButton(zipInfo)
               // post send Fetch
                Log.w("Zip_res: ", zipInfo)
            }else ->{

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            handleClick=context as handleClickFragment
            Log.i("isAttach", "works")
        }catch (e: Exception){
            Log.d("Exception Attach", e.message.toString())
        }
    }

}

interface handleClickFragment {
     fun pushOnSendButton(zipCode: String)
}