package com.amtech.baseetcustomer.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.amtech.baseetcustomer.AddService.Model.ModelPlaceOrder
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.Util
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityMainBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


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
    private var currency = ""
    private var paymentType = ""
    private var id = ""
    private var priceNew = ""
    private lateinit var bottomNav: BottomNavigationView
    private var dialog: Dialog? = null
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(Util.LocaleHelper.onAttach(newBase))
    }
    companion object {
        var statisticsList = ArrayList<ModelSpinner>()
        val car = java.util.ArrayList<ModelGetTranslatorItem>()
        val home = java.util.ArrayList<ModelGetTranslatorItem>()
        val translator = java.util.ArrayList<ModelGetTranslatorItem>()
        var back = false
        var refreshLan = true
        var refreshLanNew = true
    }

    private val NOTIFICATION_PERMISSION_CODE = 123


    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)


        MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
        updateBottomNavigationBar()
        if (sessionManager.selectedLanguage=="en"){
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        }else{
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }

        binding.imgLan.setOnClickListener {
            if (sessionManager.selectedLanguage=="en"){
                sessionManager.selectedLanguage="ar"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                updateBottomNavigationBar()
                refreshNew()
            }else{
                sessionManager.selectedLanguage="en"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                updateBottomNavigationBar()
                refreshNew()

            }

        }
        // languageDialog()

        Log.d("userIdnewwww", sessionManager.userId.toString())
        Log.d("AuthTokenNewww", sessionManager.idToken.toString())
        Log.d("fcmTokenSession", sessionManager.fcmToken.toString())
        requestNotificationPermission()
        statisticsList.clear()
        statisticsList.add(ModelSpinner(resources.getString(R.string.All_Booking), "All Booking"))
        statisticsList.add(ModelSpinner(resources.getString(R.string.pending), "Pending"))
        statisticsList.add(ModelSpinner(resources.getString(R.string.Requested), "Requested"))

        paymentResponse = intent.getStringExtra("text").toString()
        callFrom = intent!!.getStringExtra("callFrom").toString()
        foodId = intent!!.getStringExtra("foodId").toString()
        currency = intent!!.getStringExtra("currency").toString()
        paymentType = intent!!.getStringExtra("paymentType").toString()
        id = intent!!.getStringExtra("id").toString()
        priceNew = intent!!.getStringExtra("priceNew").toString()

        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.e("Notification", "onCreate: PERMISSION GRANTED")
                // sendNotification(this)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Notification blocked",
                    Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    // Responds to click on the action
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }

            else -> {
                // The registered ActivityResultCallback gets the result of this request
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
        //  binding.tvTitle.setOnClickListener {

        if (callFrom == "Payment") {
            apiCallMakeOrder()

            //   val data = "\"Array ( [website_ref_no] => 664ecf0ac305b [transaction_status] => 3 [transaction_number] => SD2623091450784 [MID] => 8962763 [RESPCODE] => 1 [RESPMSG] => Txn Success [ORDERID] => 664ecf0ac305b [STATUS] => TXN_SUCCESS [TXNAMOUNT] => 10 [issandboxmode] => 1 [checksumhash] => Nkxpg5/zh65zMj1m575AOq93WmLekli6cKattW5FJhWbhj+nO+VvSXSwZHgClxbaNkM5u92f7fyFScK0IFpJFy7Rak+BClxqjLP+vGR8sAM= ) 9H4ljR8UgNZ%2BKnHE8962763@@@@&&&&####\$\$\$\$\""

            val regex = """\[STATUS\] => (\w+)""".toRegex()
            val matchResult = regex.find(paymentResponse)

            if (matchResult != null) {
                val statusValue = matchResult.groupValues[1]
//                if (statusValue == "TXN_SUCCESS") {
                if ("1" =="1") {
                    println("STATUS value: $statusValue")
                    apiCallMakeOrder()
                    callFrom = ""
                } else {
                    println("STATUS key not found")
                    SweetAlertDialog(
                        context,
                        SweetAlertDialog.WARNING_TYPE
                    ).setTitleText(resources.getString(R.string.Payment_Field))
                        .setConfirmText(resources.getString(R.string.ok)).showCancelButton(false)
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
                    binding.tvTitle.text = resources.getString(R.string.home)
                }

                R.id.fragment_myBooking -> {
                    binding.tvTitle.text = resources.getString(R.string.My_Bookings)

                }

                R.id.fragment_Notification -> {
                    binding.tvTitle.text = resources.getString(R.string.Order_List)

                }

                R.id.fragment_Profile -> {
                    binding.tvTitle.text = resources.getString(R.string.Profile)

                }

                else -> {

                }
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                //  sendNotification(this)
                // myToast(this@MainActivity,"Permission granted")
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
        ) {
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
            NOTIFICATION_PERMISSION_CODE
        )
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "notification permission granted",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    var hasNotificationPermissionGranted = false
    private val isNotificationListenerEnabled: Boolean
        private get() {
            val pkgName = packageName
            val cn = ComponentName(pkgName, "$pkgName.NotificationListener")
            val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            return flat != null && flat.contains(cn.flattenToString())
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            // If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                Toast.makeText(
                    this,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun languageDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_langauge, null)
        dialog = Dialog(context)

        val radioArabic = view!!.findViewById<RadioButton>(R.id.radioArabic)
        val radioEnglish = view!!.findViewById<RadioButton>(R.id.radioEnglish)



        dialog = Dialog(context)
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(false)

        dialog?.show()


    }

    private fun apiCallMakeOrder() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.makeOrder(
            sessionManager.idToken.toString(),
            foodId, sessionManager.userId.toString(), id, "cash_on_delivery",
            "take_away",
            paymentType,
            "online",
            "paid",
            priceNew,
            currency
        )
            .enqueue(object : Callback<ModelPlaceOrder> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPlaceOrder>, response: Response<ModelPlaceOrder>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            SweetAlertDialog(
                                context,
                                SweetAlertDialog.SUCCESS_TYPE
                            ).setTitleText(resources.getString(R.string.Your_Order_is_Placed))
                                .setConfirmText(resources.getString(R.string.ok))
                                .showCancelButton(false)
                                .setConfirmClickListener { sDialog ->
                                    sDialog.cancel()
                                }.setCancelClickListener { sDialog ->
                                    sDialog.cancel()
                                    callFrom = ""
                                    AppProgressBar.hideLoaderDialog()
                                }.show()
                            AppProgressBar.hideLoaderDialog()
                            // onBackPressed()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
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
//      fun languageSetting(lan:String,context: Context) {
//        val locale: Locale = Locale(lan)
//        Locale.setDefault(locale)
//        val config: Configuration = Configuration()
//        config.locale = locale
//        resources.updateConfiguration(config, resources.displayMetrics)
//    }

    fun languageSetting(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
//        if (context is Activity) {
//            context.recreate()
//        }
    }



    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
        updateBottomNavigationBar()
    }

    private fun updateBottomNavigationBar() {
        binding.bottomNavigationView.menu.findItem(R.id.fragment_Home).setTitle(R.string.home)
        binding.bottomNavigationView.menu.findItem(R.id.fragment_myBooking)
            .setTitle(R.string.My_Bookings)
        binding.bottomNavigationView.menu.findItem(R.id.fragment_Notification)
            .setTitle(R.string.Order_List)
        binding.bottomNavigationView.menu.findItem(R.id.fragment_Profile).setTitle(R.string.Profile)
        // Repeat for other menu items
    }

    fun refreshNew() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }
}