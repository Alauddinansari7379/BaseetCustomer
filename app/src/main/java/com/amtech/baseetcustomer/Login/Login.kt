package com.amtech.baseetcustomer.Login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnSignIn.setOnClickListener {
                startActivity(Intent(this@Login,MainActivity::class.java))
            }
        }

    }
}