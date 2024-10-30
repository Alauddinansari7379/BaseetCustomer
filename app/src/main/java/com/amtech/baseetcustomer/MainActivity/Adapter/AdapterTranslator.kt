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
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.SingleRowTranslatorBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject


class AdapterTranslator(
    val context: Context,
    var list: ArrayList<ModelGetTranslatorItem>,/*private val videoCall: VideoCall*/
 ) : RecyclerView.Adapter<AdapterTranslator.ViewHolder>() {
    private lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowTranslatorBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleRowTranslatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager= SessionManager(context)
        try {
            with(holder) {
                with(list[position]) {
                     binding.tvName.text = context.resources.getString(R.string.name) +name
                     binding.tvLangauge.text = context.resources.getString(R.string.Language)+ tr_from +context.resources.getString(R.string.to)+tr_to
                      binding.tvTime.text = context.resources.getString(R.string.Time)+ start_time+context.resources.getString(R.string.to)+end_time
                    binding.tvType.text = type
                    binding.tvDates.text = serv_date
                    binding.tvPrice.text = price
                    binding.tvPriceN.text = "USD $price"
                     val jsonArray = JsonArray()
                    jsonArray.add("0")
                    val jsonObject = JsonObject()
                    jsonObject.add("accept_by", jsonArray)

                     val jsonString = "$accept_by"

                    println(jsonString)
                    Log.e("accept_by",jsonString)

                    if (from_ven_stts != "accept") {
                        binding.tvRequested.text =context.resources.getString(R.string.Requested)
                         binding.layoutRequested.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, com.amtech.baseetcustomer.R.color.main_color)))
                     }
//                    else if (jsonString.contains("5")&& status.toString()=="2"){
//                        binding.tvRequested.text ="Completed"
//                        binding.layoutRequested.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, com.amtech.baseetcustomer.R.color.green)))
//                    }
                    else{
                        if (stts == "reject") {
                            binding.tvRequested.text = context.resources.getString(R.string.reject)
                            binding.layoutRequested.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.main_color)))
                        } else{
                            binding.tvRequested.text =context.resources.getString(R.string.pending)
                            binding.layoutRequested.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue)))
                        }

                    }



                    binding.layoutRequested.setOnClickListener {
                        if (from_ven_stts == "accept" && serv_id.isNotEmpty() && binding.tvRequested.text!="Completed" && stts != "reject" ) {
                            var foodId = ""
                            var serviceDate = ""
                            var restId = ""
                            for (i in serv_id) {
                                foodId = i.id.toString()
                                restId = i.restaurant_id.toString()
                            }
                            if (!serv_date.isNullOrEmpty()){
                                serviceDate=serv_date.toString()
                            }
                            val i = Intent(context, BookingDetail::class.java)
                                .putExtra("callFrom", "Translator")
                                .putExtra("statues", binding.tvRequested.text.toString())
                                .putExtra("id", id.toString())
                                .putExtra("foodId", foodId)
                                .putExtra("name", name)
                                .putExtra("tr_from", tr_from)
                                .putExtra("tr_to", tr_to)
                                .putExtra("start_time", start_time)
                                .putExtra("end_time", end_time)
                                .putExtra("description", description)
                                .putExtra("country", country)
                                .putExtra("type", type)
                                .putExtra("serv_date", serviceDate)
                                .putExtra("price", price!!)
                                .putExtra("restId", restId!!)
                            context.startActivity(i)
                        }
                    }

//                    if (type=="On Call" && !jsonString.contains("0")){
//                        binding.layoutVideoCall.visibility= View.VISIBLE
//                    }else{
//                        binding.layoutVideoCall.visibility= View.GONE
//                    }
//
//
//                    binding.imgVideoCall.setOnClickListener {
//                        videoCall.videoCall("Service$customer_id")
//                    }
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
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

//    interface VideoCall{
//        fun videoCall(toString: String)
//    }

//    interface Cart{
//        fun addToCart(toString: String)
//    }
}
