package com.amtech.baseetcustomer.MainActivity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.OrderDetail
import com.amtech.baseetcustomer.Helper.pmFormate
import com.amtech.baseetcustomer.MainActivity.Model.Order
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.SingleRowOrderListBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import org.json.JSONObject


class AdapterAllOrder(
    val context: Context,
    var list: ArrayList<Order>, private val videoCall: VideoCall
) : RecyclerView.Adapter<AdapterAllOrder.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowOrderListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        try {
            with(holder) {
                with(list[position]) {
                    binding.tvSrn.text = id.toString()
                    //  binding.tvName.text = delivery_address.contact_person_name
                    binding.tvName.text = restaurant.name
                    binding.tvDate.text = created_at.subSequence(0, 11)
                    if (order_status == "delivered") {
                        binding.tvOrderStatus.text = context.resources.getString(R.string.Completed)
                    }else if (order_status == "pending"){
                        binding.tvOrderStatus.text=context.resources.getString(R.string.pending)
                    } else {
                        binding.tvOrderStatus.text = order_status

                    }
                    if (order_status == "confirmed") {
                       binding.layoutProcessing.visibility=View.VISIBLE
                       binding.layoutCom.visibility=View.GONE
                    }
                    else{
                        binding.layoutProcessing.visibility=View.GONE
                        binding.layoutCom.visibility=View.VISIBLE
                    }
                    binding.tvPaymentStatus.text = payment_status
                    binding.tvTotal.text = "$order_amount$"
                    binding.tvType.text = food_type

                    var serviceDate=""
                    var detailsNew=""
                    var type=""

                    for (i in details){
                        detailsNew= i.food_details
                        val jsonString = detailsNew
                        val jsonObject = JSONObject(jsonString)
                        serviceDate = jsonObject.getString("dates")
                        type = jsonObject.getString("drone")
//                        if (serviceDate=="null"){
//                            binding.tvServiceDate.text = "NA"
//
//                        }else{
////                            binding.tvServiceDate.text = serviceDate
//
//                        }
                    }
                    var serDate=""
                    for (i in servrequests){
                        serDate = i.serv_date
                    }
                    binding.tvServiceDate.text=serDate

                    if (type=="On Call" && restaurant.type=="translator" && order_status=="confirmed"){
                        binding.layoutVideoCall.visibility= View.VISIBLE
                    }else{
                        binding.layoutVideoCall.visibility= View.GONE
                    }


                    binding.imgVideoCall.setOnClickListener {
                        videoCall.videoCall("Service$user_id")
                    }

                    binding.layoutDetail.setOnClickListener {
                        var detailsNew=""
                        for (i in details){
                            detailsNew= i.food_details
                        }

                        val i = Intent(context, OrderDetail::class.java)
                            .putExtra("orderId", id.toString())
                            .putExtra("name", restaurant.name)
                            .putExtra("phone", restaurant.phone)
                            .putExtra("email", restaurant.email)
                            .putExtra("serviceDate", serDate)
                            .putExtra("orderStatus", order_status)
                            .putExtra("order_payment", order_payment)
                            .putExtra("pay_type", pay_type)
                            .putExtra("payment_status", payment_status)
                            .putExtra("detail",detailsNew)
                            .putExtra("order_amount", order_amount.toString())
                            .putExtra("food_type", food_type)
                        context.startActivity(i)
                    }


//                if (list[position].preview != null) {
//                    Picasso.get().load("https:"+list[position].preview)
//                        .placeholder(R.drawable.placeholder_n)
//                        .error(R.drawable.error_placeholder)
//                        .into(binding.image)
//
//                }


                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface VideoCall{
        fun videoCall(toString: String)
    }
//    interface Cart{
//        fun addToCart(toString: String)
//    }
}
