package com.amtech.baseetcustomer.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityForgotPasswordBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager

class ForgotPassword : AppCompatActivity() {
    private val binding by lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }
    private val context=this@ForgotPassword
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager=SessionManager(context)
        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())

        with(binding){
            btnSend.setOnClickListener {
                startActivity(Intent(context,OTPVerification::class.java))
            }
        }

    }
}