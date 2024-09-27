package com.amtech.baseetcustomer.MainActivity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.Model.Data
import com.amtech.baseetcustomer.AddService.OrderDetail
import com.amtech.baseetcustomer.AddService.Payment
import com.amtech.baseetcustomer.Helper.currentDate
import com.amtech.baseetcustomer.Helper.pmFormate
import com.amtech.baseetcustomer.MainActivity.Model.Order
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.SingleRowOrderListBinding
import com.amtech.baseetcustomer.databinding.SingleRowVendorListBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import org.json.JSONObject


class AdapterVendorList(
    val context: Context,
    var list: ArrayList<Data>,val sendService: SendService
) : RecyclerView.Adapter<AdapterVendorList.ViewHolder>() {
    lateinit var sessionManager: SessionManager

    inner class ViewHolder(val binding: SingleRowVendorListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleRowVendorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                    binding.tvName.text = name.toString()
                    binding.tvDate.text = created_at.substringBefore("T")
                    binding.tvType.text = food_type
                    binding.tvTraTo.text = tr_to
                    binding.tvTrFrom.text = tr_from
                    binding.tvTotal.text = price.toString()
                    binding.tvType.text = drone.toString()
                    binding.tvDescription.text = description.toString()
                    binding.btnSendService.setOnClickListener {
                        sendService.sendService(restaurant_id.toString(),id.toString())
                    }

//
//                         val i = Intent(context, Payment::class.java)
//                            .putExtra("callFrom", "Remaining")
//                            .putExtra("foodId", "foodId.toString()")
//                            .putExtra("id", id.toString())
//                            .putExtra("serviceDate", "serviceDate".toString())
//                            .putExtra("price", "result.toString()")
//                        context.startActivity(i)


                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
interface SendService{
    fun sendService(venId:String,whcSerId:String)
}
}
