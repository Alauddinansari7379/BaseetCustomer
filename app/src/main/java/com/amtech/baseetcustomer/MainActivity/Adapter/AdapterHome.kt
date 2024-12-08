package com.amtech.baseetcustomer.MainActivity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.BookingDetail
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.SingleRowCarBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso


class AdapterHome(
    val context: Context,
    var list: ArrayList<ModelGetTranslatorItem>,
) : RecyclerView.Adapter<AdapterHome.ViewHolder>() {
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
                    binding.tvName.text =  context.resources.getString(R.string.name) +name
                    binding.tvTime.text = context.resources.getString(R.string.Time)  +start_time +context.resources.getString(R.string.to) +end_time
                    binding.tvCountry.text = country
                    binding.tvDates.text = serv_date
                    binding.layoutType.text = "Type: Home"
                    binding.tvCarType.text = type
                    binding.tvPriceN.text = context.resources.getString(R.string.USD) +" "+price
                    binding.layoutDrivingType.visibility=View.GONE
                    binding.layoutTraveling.visibility=View.GONE
                    val jsonArray = JsonArray()
                    jsonArray.add("0")
                    val jsonObject = JsonObject()
                    jsonObject.add("accept_by", jsonArray)

                    val jsonString = "$accept_by"

                    println(jsonString)
                    Log.e("accept_by", jsonString)

                    if (from_ven_stts != "accept") {
                        binding.tvRequested.text = context.resources.getString(R.string.Requested)
                        binding.layoutRequested.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.main_color
                                )
                            )
                        )
                    }

//                    else if (jsonString.contains("5") && status.toString() == "2") {
//                        binding.tvRequested.text = "Completed"
//                        binding.layoutRequested.setBackgroundTintList(
//                            ColorStateList.valueOf(
//                                ContextCompat.getColor(
//                                    context,
//                                    com.amtech.baseetcustomer.R.color.green
//                                )
//                            )
//                        )
//                    }

                    else {
                        if (stts == "reject") {
                            binding.tvRequested.text = context.resources.getString(R.string.reject)
                            binding.layoutRequested.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.main_color)))
                        } else{
                            binding.tvRequested.text =context.resources.getString(R.string.pending)
                            binding.layoutRequested.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue)))
                        }

                    }


                    binding.layoutRequested.setOnClickListener {
                        try {
                            if (from_ven_stts == "accept" && serv_id.isNotEmpty() && binding.tvRequested.text!="Completed" && stts != "reject" ) {
                                var foodId = ""
                                var aminetes = ""
                                var homeType = ""
                                var serviceDate = "NA"
                                var restId = ""
                                for (i in serv_id) {
                                     restId = i.restaurant_id.toString()
                                }
                                for (i in serv_id) {
                                    foodId = i.id.toString()
                                    if (i.amenities!=null){
                                        aminetes = i.amenities!!

                                    }
                                    if (i.car_type!=null) {
                                        homeType = i.car_type!!

                                    }
                                }
                                if (!serv_date.isNullOrEmpty()) {
                                    serviceDate = serv_date.toString()
                                }
                                val i = Intent(context, BookingDetail::class.java)
                                    .putExtra("callFrom", "Home")
                                    .putExtra("statues", binding.tvRequested.text.toString())
                                    .putExtra("id", id.toString())
                                    .putExtra("foodId", foodId)
                                    .putExtra("name", name)
                                    .putExtra("tr_from", tr_from)
                                    .putExtra("order_id", order_id)
                                    .putExtra("tr_to", tr_to)
                                    .putExtra("start_time", start_time)
                                    .putExtra("end_time", end_time)
                                    .putExtra("description", description)
                                    .putExtra("country", country)
                                    .putExtra("type", type)
                                    .putExtra("trperson", trperson)
                                    .putExtra("driv_type", driv_type)
                                    .putExtra("serv_date", serviceDate)
                                    .putExtra("aminetes", aminetes)
                                    .putExtra("price", price!!)
                                    .putExtra("homeType", homeType)
                                    .putExtra("restId", restId)
                                context.startActivity(i)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

//
                    for (i in serv_id) {
                        if (i.image != null) {
                            Picasso.get().load("https://baseet.thedemostore.in/storage/app/public/product/"+i.image )
                                .placeholder(R.drawable.image)
                                .error(R.drawable.no_image_available)
                                .into(binding.image)

                        }
                    }



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
