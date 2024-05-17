package com.amtech.baseetcustomer.Translator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityTranslatorBinding

class Translator : AppCompatActivity() {
    private val binding by lazy{
        ActivityTranslatorBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(binding.root)

    }
}