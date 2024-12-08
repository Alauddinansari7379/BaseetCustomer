package com.amtech.baseetcustomer.MainActivity.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.Model.Data
import com.amtech.baseetcustomer.AddService.Translator.Companion.bookingType
import com.amtech.baseetcustomer.databinding.SingleRowVendorListBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager


class AdapterVendorList(
    val context: Context,
    var list: ArrayList<Data>, val sendService: SendService
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
                    if (bookingType == "home" || bookingType == "car") {
                        binding.layoutTrasanlotor.visibility = View.GONE
                        binding.layoutCarType.visibility = View.VISIBLE
                    }
                    binding.tvName.text = name.toString()
                    binding.tvDate.text = created_at!!.substringBefore("T")
                    when (bookingType) {
                        "home" -> {
                            binding.tvType.text = "Home"

                        }

                        "car" -> {
                            binding.tvType.text = "Car"

                        }

                        else -> {
                            binding.tvType.text = "Translator"

                        }
                    }
                    binding.tvTraTo.text = tr_to
                    binding.tvTrFrom.text = tr_from
                    binding.tvCarType.text = ": $car_type"
                    if (bookingType == "home") {
                        binding.tvCarTypeHN.text = "Home Type"
                    }
                    binding.tvDriveType.text = ": $driv_type"
                    binding.tvTotal.text = price.toString()
                    //  binding.tvType.text = drone.toString()
                    binding.tvDescription.text = description.toString()

                    binding.btnSendService.setOnClickListener {
                        sendService.sendService(restaurant_id.toString(), id.toString())
                        removeItem(position) // Remove the item

                    }
                    binding.layoutDetail.setOnClickListener {
                        sendService.showDetailsPopup(food_type.toString(), id.toString())
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

    private fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    interface SendService {
        fun sendService(venId: String, whcSerId: String)
        fun showDetailsPopup(venId: String, whcSerId: String)
    }

}
