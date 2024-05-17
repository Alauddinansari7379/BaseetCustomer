package com.amtech.baseetcustomer.AddService

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityHomeRentalBinding

class HomeRental : AppCompatActivity() {
    private val binding by lazy { ActivityHomeRentalBinding.inflate(layoutInflater) }
    private val context=this@HomeRental
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