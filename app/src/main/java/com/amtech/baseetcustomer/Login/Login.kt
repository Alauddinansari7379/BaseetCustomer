package com.amtech.baseetcustomer.Login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.SignUp.ForgotPassword
import com.amtech.baseetcustomer.SignUp.SignUp
import com.amtech.baseetcustomer.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    val context=this@Login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnSignIn.setOnClickListener {
                startActivity(Intent(this@Login, MainActivity::class.java))
            }
            passwordToggle.setOnClickListener {
                passwordToggleOff.visibility = View.VISIBLE
                passwordToggle.visibility = View.GONE
                edtPassword.transformationMethod = PasswordTransformationMethod()
            }
            passwordToggleOff.setOnClickListener {
                passwordToggleOff.visibility = View.GONE
                passwordToggle.visibility = View.VISIBLE
                edtPassword.transformationMethod = null
            }
            layoutCreate.setOnClickListener {
                startActivity(Intent(context,SignUp::class.java))
            }

            layoutForgot.setOnClickListener {
                startActivity(Intent(context,ForgotPassword::class.java))
            }
        }
    }
}