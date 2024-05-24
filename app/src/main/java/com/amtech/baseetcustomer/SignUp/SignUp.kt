package com.amtech.baseetcustomer.SignUp

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.Login.Login
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    val context = this@SignUp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}


