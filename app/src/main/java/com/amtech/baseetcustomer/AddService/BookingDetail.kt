package com.amtech.baseetcustomer.AddService

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.databinding.ActivityBookingDetailBinding

class BookingDetail : AppCompatActivity() {
    private val binding by lazy { ActivityBookingDetailBinding.inflate(layoutInflater) }
    private val context = this@BookingDetail
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