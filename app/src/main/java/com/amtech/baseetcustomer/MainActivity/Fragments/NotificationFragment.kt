package com.amtech.baseetcustomer.MainActivity.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterAllOrder
import com.amtech.baseetcustomer.MainActivity.Model.ModelAllOrder
import com.amtech.baseetcustomer.MainActivity.Model.Order
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentMyBookingBinding
import com.amtech.baseetcustomer.databinding.FragmentNotificationBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationFragment : Fragment() {
    private lateinit var  binding: FragmentNotificationBinding
    lateinit var sessionManager: SessionManager
    var count=0
    private lateinit var mainData: ArrayList<Order>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentNotificationBinding.bind(view)
        sessionManager= SessionManager(requireActivity())
        mainData=ArrayList<Order>()
        with(binding){

            apiCallAllOrder()

            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.restaurant.name != null && it.restaurant.name.toString()!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Order>)
            }
        }

    }
    private fun apiCallAllOrder() {
         AppProgressBar.showLoaderDialog(requireActivity())
        ApiClient.apiService.orderList(
            sessionManager.idToken.toString(),
            "10","1"
        )
            .enqueue(object : Callback<ModelAllOrder> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAllOrder>, response: Response<ModelAllOrder>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        }
                        else if  (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.orders.isEmpty()) {
                            myToast(requireActivity(), "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.orders
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(requireActivity(), "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelAllOrder>, t: Throwable) {
                    myToast(requireActivity(), t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallAllOrder()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }
    private fun setRecyclerViewAdapter(data: ArrayList<Order>) {
        binding.recyclerView.apply {
            adapter = AdapterAllOrder(requireActivity(), data)
            AppProgressBar.hideLoaderDialog()

        }
    }

}