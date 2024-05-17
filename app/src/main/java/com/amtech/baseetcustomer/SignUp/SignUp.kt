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
    val context=this@SignUp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding) {
            passwordToggle.setOnClickListener {
                passwordToggleOff.visibility = View.VISIBLE
                passwordToggle.visibility = View.GONE
                edtPassword.transformationMethod = PasswordTransformationMethod()
            }
            passwordToggleOff.setOnClickListener {
                passwordToggleOff.visibility = View.GONE
                passwordToggle.visibility = View.VISIBLE
                edtPassword.transformationMethod = null
                //binding.passwordEdt.transformationMethod =PasswordTransformationMethod(false)
                //binding.passwordToggle.sw
//        }
            }
            layoutLogin.setOnClickListener {
                startActivity(Intent(context,Login::class.java))
            }

            btnSignUp.setOnClickListener {
                startActivity(Intent(context,Login::class.java))
            }
        }
    }
}