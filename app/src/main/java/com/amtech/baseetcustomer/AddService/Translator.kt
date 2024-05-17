package com.amtech.baseetcustomer.AddService

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.databinding.ActivityTranslatorBinding

class Translator : AppCompatActivity() {
    private val binding by lazy{
        ActivityTranslatorBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(binding.root)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

    }
}