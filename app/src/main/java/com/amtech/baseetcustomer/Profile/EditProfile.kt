package com.amtech.baseetcustomer.Profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityEditProfileBinding

class EditProfile : AppCompatActivity() {
    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
    private val context = this@EditProfile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

        }
    }
}

