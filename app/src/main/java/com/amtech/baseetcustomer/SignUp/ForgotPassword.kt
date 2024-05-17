package com.amtech.baseetcustomer.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    private val binding by lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }
    private val context=this@ForgotPassword
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnSend.setOnClickListener {
                startActivity(Intent(context,OTPVerification::class.java))
            }
        }

    }
}