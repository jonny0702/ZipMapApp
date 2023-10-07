package com.example.zipmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class InputZipCode : Fragment() {


    private var zipCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            zipCode = it.getString(ZIP_CODE_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_zip_code, container, false)
    }

    companion object{
        private const val  ZIP_CODE_BUNDLE = "507"
        @JvmStatic
        fun newInstance(zipCode: String) = InputZipCode().apply {
            arguments = Bundle().apply {
                putString(ZIP_CODE_BUNDLE, zipCode)
            }
        }

    }
}