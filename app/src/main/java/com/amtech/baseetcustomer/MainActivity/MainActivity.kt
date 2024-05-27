package com.amtech.baseetcustomer.MainActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtech.baseetcustomer.AddService.Model.ModelPlaceOrder
import com.amtech.baseetcustomer.AddService.Payment
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.Login.Login
import com.amtech.baseetcustomer.MainActivity.Model.ModelAllOrder
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityMainBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var context = this@MainActivity
    lateinit var sessionManager: SessionManager
    var count = 0
    var countDes = 0
    private var paymentResponse = ""
    private var callFrom = ""
    private var foodId = ""
    private var id = ""
    private lateinit var bottomNav: BottomNavigationView
    companion object{
        var statisticsList = ArrayList<ModelSpinner>()
        val car = java.util.ArrayList<ModelGetTranslatorItem>()
        val home = java.util.ArrayList<ModelGetTranslatorItem>()
        val translator = java.util.ArrayList<ModelGetTranslatorItem>()
        var back = false

    }
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sessionManager = SessionManager(context)
        Log.d("userIdnewwww",sessionManager.userId.toString())
        Log.d("AuthTokenNewww",sessionManager.idToken.toString())

        statisticsList.clear()
        statisticsList.add(ModelSpinner("All Booking", "1"))
        statisticsList.add(ModelSpinner("Pending", "1"))
        statisticsList.add(ModelSpinner("Requested", "1"))

         paymentResponse= intent.getStringExtra("text").toString()
         callFrom= intent!!.getStringExtra("callFrom").toString()
        foodId= intent!!.getStringExtra("foodId").toString()
        id= intent!!.getStringExtra("foodId").toString()


      //  binding.tvTitle.setOnClickListener {

            if (callFrom == "Payment") {
             //   val data = "\"Array ( [website_ref_no] => 664ecf0ac305b [transaction_status] => 3 [transaction_number] => SD2623091450784 [MID] => 8962763 [RESPCODE] => 1 [RESPMSG] => Txn Success [ORDERID] => 664ecf0ac305b [STATUS] => TXN_SUCCESS [TXNAMOUNT] => 10 [issandboxmode] => 1 [checksumhash] => Nkxpg5/zh65zMj1m575AOq93WmLekli6cKattW5FJhWbhj+nO+VvSXSwZHgClxbaNkM5u92f7fyFScK0IFpJFy7Rak+BClxqjLP+vGR8sAM= ) 9H4ljR8UgNZ%2BKnHE8962763@@@@&&&&####\$\$\$\$\""

                val regex = """\[STATUS\] => (\w+)""".toRegex()
                val matchResult = regex.find(paymentResponse)

                if (matchResult != null) {
                    val statusValue = matchResult.groupValues[1]
                    if (statusValue=="TXN_SUCCESS"){
                        println("STATUS value: $statusValue")
                        apiCallMakeOrder()
                    } else
                 {
                    println("STATUS key not found")
                    SweetAlertDialog(
                        context,
                        SweetAlertDialog.WARNING_TYPE
                    ).setTitleText("Payment Field")
                        .setConfirmText("ok").showCancelButton(false)
                        .setConfirmClickListener { sDialog ->
                            sDialog.cancel()
                        }.setCancelClickListener { sDialog ->
                            sDialog.cancel()
                        }.show()
                }
                    }

            }
       // }



        bottomNav = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)
        val navController = navHostFragment!!.findNavController()
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bootom_nav_menu)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_Home -> {
                    binding.tvTitle.text = "Home"

                }

                R.id.fragment_myBooking -> {
                    binding.tvTitle.text = "My Bookings"

                }

                R.id.fragment_Notification -> {
                    binding.tvTitle.text = "Order List"

                }

                R.id.fragment_Profile -> {
                    binding.tvTitle.text = "Profile"

                }

                else -> {

                }
            }
        }

    }
    private fun apiCallMakeOrder() {
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
                            SweetAlertDialog(
                                context,
                                SweetAlertDialog.SUCCESS_TYPE
                            ).setTitleText("Your Order is Placed")
                                .setConfirmText("ok").showCancelButton(false)
                                .setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
                                }.setCancelClickListener { sDialog ->
                                    sDialog.cancel()
                                    AppProgressBar.hideLoaderDialog()
                                }.show()
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
                        apiCallMakeOrder()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }

}