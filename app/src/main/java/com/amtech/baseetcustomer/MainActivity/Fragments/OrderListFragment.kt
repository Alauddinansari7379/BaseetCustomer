package com.amtech.baseetcustomer.MainActivity.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterAllOrder
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.Model.ModelAllOrder
import com.amtech.baseetcustomer.MainActivity.Model.ModelCancel.ModelCancel
import com.amtech.baseetcustomer.MainActivity.Model.Order
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentNotificationBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL


 class OrderListFragment : Fragment(),AdapterAllOrder.VideoCall {
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
        activity?.let { MainActivity().languageSetting(it,sessionManager.selectedLanguage.toString()) }

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
            sessionManager.idToken.toString(), "100","1")
            .enqueue(object : Callback<ModelAllOrder> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAllOrder>, response: Response<ModelAllOrder>
                ) {
                    try {
                        if (response.code() == 404) {
                            activity?.let { myToast(it,  resources.getString(R.string.Something_went_wrong)) }
                            AppProgressBar.hideLoaderDialog()

                        }
                        else if  (response.code() == 500) {
                            activity?.let { myToast(it,  resources.getString(R.string.Server_Error))
                            }
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.orders.isEmpty()) {
                            activity?.let { myToast(it,  resources.getString(R.string.No_Data_Found)) }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!.orders
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        activity?.let { myToast(it,  resources.getString(R.string.Something_went_wrong)) }
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }
                override fun onFailure(call: Call<ModelAllOrder>, t: Throwable) {
                    activity?.let { myToast(it, t.message.toString()) }
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallAllOrder()
                    } else {
                        activity?.let { myToast(it, t.message.toString()) }
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }
    private fun setRecyclerViewAdapter(data: ArrayList<Order>) {
        binding.recyclerView.apply {
            adapter = AdapterAllOrder(requireActivity(), data,this@OrderListFragment)
            AppProgressBar.hideLoaderDialog()

        }
    }

    override fun onResume() {
        super.onResume()
        apiCallAllOrder()
    }

    override fun videoCall(toString: String) {
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.customerName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://ka-nnect.com/"))
                .setRoom(toString)
                .setAudioMuted(false)
                .setVideoMuted(true)
                .setAudioOnly(false)
                .setUserInfo(jitsiMeetUserInfo)
                .setConfigOverride("enableInsecureRoomNameWarning", false)
                .setFeatureFlag("readOnlyName", true)
                .setFeatureFlag("prejoinpage.enabled", false)
                //  .setFeatureFlag("lobby-mode.enabled", false)
                // .setToken("123") // Set the meeting password
                //.setFeatureFlag("autoKnockLobby", false) // Disable lobby mode
                //.setFeatureFlag("disableModeratorIndicator", false)
                //.setFeatureFlag("chat.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            JitsiMeetActivity.launch(context, defaultOptions)

            //  startActivity(Intent(requireContext(),Rating::class.java))
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    override fun viewDoc(url: String?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_document, null)
        val imgDoc = dialogView.findViewById<ImageView>(R.id.imgDoc)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClose)

        val close = dialogView.findViewById<ImageView>(R.id.imgClose)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        close.setOnClickListener {
            dialog.dismiss()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        if (url != null) {
            Picasso.get().load("https://baseet.thedemostore.in/storage/app/public/product/" + url)
                .error(R.drawable.error_placeholder)
                .into(imgDoc)
        }
        dialog.show()

    }

     override fun cancelOrder(orderId: String) {
         apiCallCancelOrder(orderId)
     }

     private fun apiCallCancelOrder(orderId: String) {
         AppProgressBar.showLoaderDialog(context)
         ApiClient.apiService.cancelOrder(
             sessionManager.idToken.toString(), orderId
         ).enqueue(object :
             Callback<ModelCancel> {
             @SuppressLint("LogNotTimber", "LongLogTag", "SetTextI18n")
             override fun onResponse(
                 call: Call<ModelCancel>,
                 response: Response<ModelCancel>
             ) {
                 try {
                     if (response.code() == 500) {
                         activity?.let { myToast(it, resources.getString(R.string.Server_Error)) }
                         AppProgressBar.hideLoaderDialog()

                     } else if (response.code() == 401) {
                         activity?.let { myToast(it, resources.getString(R.string.Unauthorized)) }
                         AppProgressBar.hideLoaderDialog()

                     } else {

                         if (response.isSuccessful) {
                             activity?.let {
                                 myToast(it, resources.getString(R.string.order_cancel_successfully))
                                 apiCallAllOrder()
                             }
                         }else{
                             activity?.let {
                                 myToast(
                                     it,
                                     resources.getString(R.string.Something_went_wrong)
                                 )
                             }
                             AppProgressBar.hideLoaderDialog()
                         }

                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     activity?.let {
                         myToast(
                             it,
                             resources.getString(R.string.Something_went_wrong)
                         )
                     }
                     AppProgressBar.hideLoaderDialog()

                 }
             }

             override fun onFailure(call: Call<ModelCancel>, t: Throwable) {
                 AppProgressBar.hideLoaderDialog()
                 count++
                 if (count <= 3) {
                     Log.e("count", count.toString())
                     apiCallCancelOrder(orderId)
                 } else {
                     activity?.let { myToast(it, t.message.toString()) }
                     AppProgressBar.hideLoaderDialog()

                 }
                 AppProgressBar.hideLoaderDialog()
             }

         })
     }

 }