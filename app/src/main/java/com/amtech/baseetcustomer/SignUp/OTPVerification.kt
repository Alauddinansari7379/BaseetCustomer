package com.amtech.baseetcustomer.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityOtpverificationBinding

class OTPVerification : AppCompatActivity() {
    private val binding by lazy { ActivityOtpverificationBinding.inflate(layoutInflater) }
    private val context=this@OTPVerification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(binding.root)
        with(binding){
            with(binding){
                btnSubmit.setOnClickListener {
                    startActivity(Intent(context,ResetPassword::class.java))
                }
            }
        }

    }
}