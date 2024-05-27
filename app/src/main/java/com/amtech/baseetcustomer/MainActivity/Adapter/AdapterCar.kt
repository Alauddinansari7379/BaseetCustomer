package com.amtech.baseetcustomer.MainActivity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.BookingDetail
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.databinding.SingleRowCarBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject


class AdapterCar(
    val context: Context,
    var list: ArrayList<ModelGetTranslatorItem>,
) : RecyclerView.Adapter<AdapterCar.ViewHolder>() {
    private lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowCarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        try {
            with(holder) {
                with(list[position]) {
                    binding.tvName.text = "Name : $name"
                    binding.tvTime.text = "Time :  $start_time ${"to"} $end_time"
                    binding.tvCountry.text = country
                    binding.tvDates.text = serv_date
                    binding.tvPriceN.text = "USD $price"
                    val jsonArray = JsonArray()
                    jsonArray.add("0")
                    val jsonObject = JsonObject()
                    jsonObject.add("accept_by", jsonArray)

                    val jsonString = "$accept_by"

                    println(jsonString)
                    Log.e("accept_by", jsonString)

                    if (jsonString.contains("0")) {
                        binding.tvRequested.text = "Requested"
                        binding.layoutRequested.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    com.amtech.baseetcustomer.R.color.main_color
                                )
                            )
                        )
                    } else {
                        binding.tvRequested.text = "Pending"
                        binding.layoutRequested.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    com.amtech.baseetcustomer.R.color.blue
                                )
                            )
                        )
                    }

//

                    binding.layoutRequested.setOnClickListener {
                             if (serv_id.isNotEmpty()) {
                                var foodId = ""
                                var serviceDate = ""
                                for (i in serv_id) {
                                    foodId = i.id.toString()
                                }
                                if (!serv_date.isNullOrEmpty()){
                                    serviceDate=serv_date.toString()
                                }
                                val i = Intent(context, BookingDetail::class.java)
                                    .putExtra("callFrom", "car")
                                    .putExtra("statues", binding.tvRequested.text.toString())
                                    .putExtra("id", id)
                                    .putExtra("name", name)
                                    .putExtra("foodId", foodId)
                                    .putExtra("tr_from", tr_from)
                                    .putExtra("tr_to", tr_to)
                                    .putExtra("start_time", start_time)
                                    .putExtra("end_time", end_time)
                                    .putExtra("description", description)
                                    .putExtra("country", country)
                                    .putExtra("type", type)
                                    .putExtra("trperson", trperson)
                                    .putExtra("driv_type", driv_type)
                                    .putExtra("serv_date", serviceDate)
                                    .putExtra("price", price!!)
                                context.startActivity(i)
                        }
                    }

//
//                if (list[position].serv_id.appimage != null) {
//                    Picasso.get().load("https:"+list[position].serv_id.appimage)
//                        .placeholder(R.drawable.usernew)
//                        .error(R.drawable.usernew)
//                        .into(binding.image)
//
//                }


                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    interface Cart{
//        fun addToCart(toString: String)
//    }
}
