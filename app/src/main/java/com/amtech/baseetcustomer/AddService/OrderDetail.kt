package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityOrderDetialBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import org.json.JSONObject

class OrderDetail : AppCompatActivity() {
    private val binding by lazy {
        ActivityOrderDetialBinding.inflate(layoutInflater)
    }
    lateinit var sessionManager: SessionManager
    private val context = this@OrderDetail

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())
        if (MainActivity.refreshLanNew) {
            MainActivity.refreshLanNew = false
            refresh()
        }
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
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            val type = intent.getStringExtra("food_type").toString()
            val orderStatues = intent.getStringExtra("orderStatus").toString()
            val detail = intent.getStringExtra("detail").toString()

            val jsonString = detail

            val jsonObject = JSONObject(jsonString)

            val nameNew = jsonObject.getString("name")
            val amenities = jsonObject.getString("amenities")
            val rent_typ = jsonObject.getString("rent_typ")
            val trFrom = jsonObject.getString("tr_from")
            val trTo = jsonObject.getString("tr_to")
            val trPerson = jsonObject.getString("trperson")
            val drivType = jsonObject.optString("driv_type", "N/A")



            when (type) {
                "translator" -> {
                    layoutTravling.visibility = View.GONE
                    layoutDrivingType.visibility = View.GONE
                    layoutRentType.visibility = View.GONE
                    layoutAminites.visibility = View.GONE
                    layoutCar.visibility = View.GONE
                }

                "home" -> {
                    layoutTravling.visibility = View.GONE
                    layoutDrivingType.visibility = View.GONE
                    layoutTraTo.visibility = View.GONE
                    layoutCar.visibility = View.GONE


                }

                else -> {
                    layoutRentType.visibility = View.GONE
                    layoutAminites.visibility = View.GONE
                    layoutTraTo.visibility = View.GONE

                }
            }

            orderId.text = intent.getStringExtra("orderId").toString()
            tvCusName.text = intent.getStringExtra("name").toString()
            date.text = intent.getStringExtra("serviceDate").toString()
            orderId.text = intent.getStringExtra("orderId").toString()
            tvCarType.text = intent.getStringExtra("food_type").toString()
            tvTotal.text = intent.getStringExtra("order_amount").toString() + "$"
            tvEmail.text = intent.getStringExtra("email").toString()
            tvOrderPayment.text = intent.getStringExtra("order_payment").toString()
            tvPayType.text = intent.getStringExtra("pay_type").toString()
            tvNumber.text = intent.getStringExtra("phone").toString()
            tvName.text = nameNew
            tvAmenities.text = amenities
            tvRentType.text = rent_typ
            tvTravelingPersons.text = trPerson
            tvTrFrom.text = resources.getString(R.string.from)+trFrom
            tvTraTo.text =resources.getString(R.string.to)+trTo
            tvDrivingType.text = drivType

            if (orderStatues == "confirmed") {
                binding.layoutProcessing.visibility=View.VISIBLE
                binding.layoutCon.visibility=View.GONE
            } else{
                binding.layoutProcessing.visibility=View.GONE
                binding.layoutCon.visibility=View.VISIBLE
            }
            tvPaymentStatus.text = intent.getStringExtra("payment_status").toString()
            if (orderStatues == "delivered") {
                tvOrderStatus.text = resources.getString(R.string.Completed)
            } else {
                tvOrderStatus.text = intent.getStringExtra("orderStatus").toString()

            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        MainActivity.refreshLanNew=true
    }
    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}