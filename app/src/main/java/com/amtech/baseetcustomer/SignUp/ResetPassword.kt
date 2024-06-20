package com.amtech.baseetcustomer.SignUp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.Login.Login
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityResetPasswordBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager

class ResetPassword : AppCompatActivity() {
    private val binding by lazy { ActivityResetPasswordBinding.inflate(layoutInflater) }
    private val context=this@ResetPassword
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(binding.root)
        sessionManager= SessionManager(context)
        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())
        with(binding){
            btnReset.setOnClickListener {
                startActivity(Intent(context,Login::class.java))
            }
        }

    }
}