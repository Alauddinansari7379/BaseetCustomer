package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.AddService.Model.ModelPlaceOrder
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.isOnline
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.back
import com.amtech.baseetcustomer.databinding.ActivityBookingDetailBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class BookingDetail : AppCompatActivity() {
    private val binding by lazy { ActivityBookingDetailBinding.inflate(layoutInflater) }
    private val context = this@BookingDetail
    var count = 0
    var id = ""
    private var foodId = ""
    private var price = ""
    private lateinit var sessionManager: SessionManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            try {

                val callFrom = intent.getStringExtra("callFrom")
                id = intent.getStringExtra("id").toString()
                foodId = intent.getStringExtra("foodId").toString()
                val statues = intent.getStringExtra("statues")
                val name = intent.getStringExtra("name")
                val trFrom = intent.getStringExtra("tr_from")
                val trTo = intent.getStringExtra("tr_to")
                val startTime = intent.getStringExtra("start_time")
                val endTime = intent.getStringExtra("end_time")
                val description = intent.getStringExtra("description")
                val country = intent.getStringExtra("country")
                val type = intent.getStringExtra("type")
                val serviceDate = intent.getStringExtra("serv_date")
                  price = intent!!.getStringExtra("price").toString()
                val trPerson = intent.getStringExtra("trperson")
                val driverType = intent.getStringExtra("driv_type")
                val aminetes = intent.getStringExtra("aminetes")
                val homeType = intent.getStringExtra("homeType")

                when (callFrom) {
                    "Translator" -> {
                        image.visibility=View.GONE
                        layoutHomeDetial.visibility=View.GONE
                        tvName.text = name
                        tvStatues.text = statues
                        tvFromTo.text = trFrom + " To " + trTo
                        tvTime.text = startTime + " To " + endTime
                        tvDescription.text = description
                        tvCountry.text = "Country Name : " + country
                        tvDoc.text = type
                        tvDates.text = serviceDate
                        tvPrice.text = "Price : $" + price
                    }

                    "car" -> {
                        layoutProfile.visibility=View.GONE
                        layoutHomeDetial.visibility=View.GONE
                        tvDescriptionH.text = "Comment"
                        tvName.text = name
                        tvStatues.text = statues
                        tvFromTo.text ="No Of Person : $trPerson"
                        tvTime.text = startTime + " To " + endTime
                        tvDescription.text = description
                        tvCountry.text = "Country Name : " + country
                        tvDoc.text = driverType
                        tvDates.text = serviceDate
                        tvPrice.text = "Price : $" + price

                    }

                    else -> {
                        layoutProfile.visibility=View.GONE
                        tvFromTo.text ="No Of Family : $trPerson"
                        tvHomeDetail.text =aminetes
                        tvDoc.text ="Home Type : $homeType"
                        tvName.text = name
                        tvStatues.text = statues
                         tvTime.text = startTime + " To " + endTime
                        tvDescription.text = description
                        tvCountry.text = "Country Name : " + country
                         tvDates.text = serviceDate
                        tvPrice.text = "Price : $" + price


                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btnAccept.setOnClickListener {
                apiCallAccept()
            }

        }
    }

    private fun apiCallAccept() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.makeOrder(
            sessionManager.idToken.toString(),
            foodId, sessionManager.userId.toString(), id, "cash_on_delivery", "take_away"
        )
            .enqueue(object : Callback<ModelPlaceOrder> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPlaceOrder>, response: Response<ModelPlaceOrder>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(context, "Order Accepted")
                                 val i = Intent(context, Payment::class.java)
                            .putExtra("callFrom", "Booking")
                                .putExtra("price", price)
                            context.startActivity(i)
                            AppProgressBar.hideLoaderDialog()
                           // onBackPressed()
                        }
                    } catch (e: Exception) {
                        myToast(context, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelPlaceOrder>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallAccept()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(context)) {
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        back = true
    }
}