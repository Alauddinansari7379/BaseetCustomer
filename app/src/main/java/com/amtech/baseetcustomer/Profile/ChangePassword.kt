package com.amtech.baseetcustomer.Profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityChangePasswordBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager

class ChangePassword : AppCompatActivity() {
    private val binding by lazy { ActivityChangePasswordBinding.inflate(layoutInflater) }
    private val context=this@ChangePassword
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(binding.root)
        sessionManager=SessionManager(context)
         MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())
        if (sessionManager.selectedLanguage == "en") {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        } else {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }

        binding.imgLan.setOnClickListener {
            if (sessionManager.selectedLanguage == "en") {
                sessionManager.selectedLanguage = "ar"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                sessionManager.selectedLanguage = "en"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }
        }

    }
}