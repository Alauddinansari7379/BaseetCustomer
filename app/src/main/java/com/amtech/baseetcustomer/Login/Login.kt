package com.amtech.baseetcustomer.Login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.isOnline
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.Login.model.ModelLogin
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.refreshLan
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.SignUp.ForgotPassword
import com.amtech.baseetcustomer.SignUp.SignUp
import com.amtech.baseetcustomer.databinding.ActivityLoginBinding
import com.amtech.baseetcustomer.otpless.ConfigurationSettings
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.google.gson.JsonParser
import com.otpless.dto.OtplessRequest
import com.otpless.dto.OtplessResponse
import com.otpless.main.OtplessManager
import com.otpless.main.OtplessView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.Locale

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    val context = this@Login
    private lateinit var sessionManager: SessionManager
    var count = 0
    var countlogin = 0
    private val PREF_NAME = "MyPrefs"
    private val PREF_USERNAME = "username"
    private val PREF_PASSWORD = "password"
    private val FCM_TOKEN = "fcmtoken"
    private var fcmTokenNew = ""
    private var dialog: Dialog? = null
    private lateinit var otplessView: OtplessView

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeOtpless()
        sessionManager = SessionManager(context)

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        Log.e("sessionManager.fcmToken", sessionManager.fcmToken.toString())



        if (sessionManager.selectedLanguage.isNullOrEmpty()) {
            languageDialog()
        } else {
            if (refreshLan) {
                refresh()
                refreshLan = false
            }
            langaugeSetting()
//            binding.edtEmail.gravity = Gravity.END
//            binding.edtPassword.gravity = Gravity.END
        }
//9876543211
        if (sessionManager.isLogin) {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }


        with(binding) {
            btnSignIn.setOnClickListener {
                startActivity(Intent(this@Login, MainActivity::class.java))
            }
            passwordToggle.setOnClickListener {
                passwordToggleOff.visibility = View.VISIBLE
                passwordToggle.visibility = View.GONE
                edtPassword.transformationMethod = PasswordTransformationMethod()
            }
            passwordToggleOff.setOnClickListener {
                passwordToggleOff.visibility = View.GONE
                passwordToggle.visibility = View.VISIBLE
                edtPassword.transformationMethod = null
            }
            layoutCreate.setOnClickListener {
                startActivity(Intent(context, SignUp::class.java))
            }

            layoutForgot.setOnClickListener {
                startActivity(Intent(context, ForgotPassword::class.java))
            }
            btnSignIn.setOnClickListener {
                showPreBuiltUI()
//                if (edtEmail.text!!.isEmpty()) {
//                    edtEmail.error = resources.getString(R.string.Enter_Email)
//                    edtEmail.requestFocus()
//                    return@setOnClickListener
//                }
//                if (edtPassword.text!!.isEmpty()) {
//                    edtPassword.error = resources.getString(R.string.Enter_Password)
//                    edtPassword.requestFocus()
//                    return@setOnClickListener
//                }
//                if (sessionManager.fcmToken!!.isNotEmpty()) {
//                    saveFCM(sessionManager.fcmToken.toString())
//                }
//                fcmTokenNew = sharedPreferences.getString(FCM_TOKEN, "").toString()
//
//                Log.e("FCMNewSession", fcmTokenNew)
//                apiCallLogin()
            }
        }
    }

    private fun initializeOtpless() {
        // Initialise OtplessView in case of both Activity and Fragment
        otplessView = OtplessManager.getInstance().getOtplessView(this)
    }

    private fun showPreBuiltUI() {
        val request = OtplessRequest(ConfigurationSettings.APP_ID)
        otplessView.setCallback(request, this::onOtplessCallback)
        otplessView.showOtplessLoginPage(request, this::onOtplessCallback)
    }

    private fun onOtplessCallback(response: OtplessResponse) {
//        otplessResponseHandler.visibility = View.VISIBLE
        val response =
            if (response.errorMessage != null) response.errorMessage else response.data.toString()


        val jsonString = """$response"""

        // Parse the JSON
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject

        // Extract values
        val status = jsonObject.get("status").asString
        val mobile = jsonObject.getAsJsonArray("identities")
            .first { it.asJsonObject.get("identityType").asString == "MOBILE" }
            .asJsonObject.get("identityValue").asString

        println("Status: $status")
        println("Mobile: $mobile")
        if (status == "SUCCESS"){

            apiCallLogin(mobile.substringAfter("91"))
        }else{
            myToast(context,"Try Again: Verification Failed")
        }

        Log.i("ResponseOtpless",response)
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        otplessView.onNewIntent(intent)
    }

    override fun onBackPressed() {
        // make sure you call this code before super.onBackPressed();
        if (otplessView.onBackPressed()) return
        super.onBackPressed()
    }
    private fun apiCallLogin(mobileNo: String) {
        AppProgressBar.showLoaderDialog(this@Login)
        ApiClient.apiService.login(
            mobileNo,
            fcmTokenNew,
            "android",
        ).enqueue(object :
            Callback<ModelLogin> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelLogin>,
                response: Response<ModelLogin>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Login, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    }  else {
                        sessionManager.isLogin = true
                        sessionManager.idToken = "Bearer " + response.body()!!.token
                        sessionManager.userId = response.body()!!.user_id.toString()
                        //apiCallGetProfile()

                        Handler(Looper.getMainLooper()).postDelayed({
                            AppProgressBar.hideLoaderDialog()

                            Log.e("sessionManager.idToken", sessionManager.idToken.toString())
                            Log.e("sessionManager.userId", sessionManager.userId.toString())
                            Log.e("sessionManager.group", sessionManager.group.toString())
                            myToast(this@Login, resources.getString(R.string.Login_Sucessfully))
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)

                        }, 300)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Login, resources.getString(R.string.Try_Again))
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {
                myToast(this@Login, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
                countlogin++
                if (countlogin <= 3) {
                    Log.e("count", countlogin.toString())
                    apiCallLogin(mobileNo)
                } else {
                    myToast(this@Login, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun saveFCM(fcmToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(FCM_TOKEN, fcmToken)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(context)) {
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()

        }
    }

    private fun languageDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_langauge, null)
        dialog = Dialog(context)


        val radioEnglish = view!!.findViewById<RadioButton>(R.id.radioEnglish)
        val radioArabic = view!!.findViewById<RadioButton>(R.id.radioArabic)


        radioEnglish.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "en"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "en"
            dialog?.dismiss()

        }
        radioArabic.setOnCheckedChangeListener { _, _ ->
            val languageToLoad = "ar"
            val locale: Locale = Locale(languageToLoad)
            Locale.setDefault(locale)
            val config: Configuration = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            sessionManager.selectedLanguage = "ar"

            dialog?.dismiss()
            refresh()
        }

        dialog = Dialog(context)
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(false)

        dialog?.show()


    }

    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun langaugeSetting() {
        val locale: Locale = Locale(sessionManager.selectedLanguage!!)
        Locale.setDefault(locale)
        val config: Configuration = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}