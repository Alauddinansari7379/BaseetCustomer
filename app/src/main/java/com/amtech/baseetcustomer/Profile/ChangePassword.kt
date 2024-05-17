package com.amtech.baseetcustomer.Profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityChangePasswordBinding

class ChangePassword : AppCompatActivity() {
    private val binding by lazy { ActivityChangePasswordBinding.inflate(layoutInflater) }
    private val context=this@ChangePassword
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