package com.amtech.baseetcustomer.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityOtpverificationBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager

class OTPVerification : AppCompatActivity() {
    private val binding by lazy { ActivityOtpverificationBinding.inflate(layoutInflater) }
    private val context=this@OTPVerification
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(binding.root)
        sessionManager=SessionManager(context)
        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())

        with(binding){
            with(binding){
                btnSubmit.setOnClickListener {
                    startActivity(Intent(context,ResetPassword::class.java))
                }
            }
        }

    }
}