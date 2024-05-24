package com.amtech.baseetcustomer.Login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.isOnline
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.Login.model.ModelLogin
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.SignUp.ForgotPassword
import com.amtech.baseetcustomer.SignUp.SignUp
import com.amtech.baseetcustomer.databinding.ActivityLoginBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.MyTranslotor.Model.ModelMyTra
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    val context=this@Login
    private lateinit var  sessionManager: SessionManager
    var count = 0
    var countlogin = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager=SessionManager(context)

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
                startActivity(Intent(context,SignUp::class.java))
            }

            layoutForgot.setOnClickListener {
                startActivity(Intent(context,ForgotPassword::class.java))
            }
            btnSignIn.setOnClickListener {
                if (edtEmail.text!!.isEmpty()) {
                    edtEmail.error = "Enter Email"
                    edtEmail.requestFocus()
                    return@setOnClickListener
                }
                if (edtPassword.text!!.isEmpty()) {
                    edtPassword.error = "Enter Password"
                    edtPassword.requestFocus()
                    return@setOnClickListener
                }

                apiCallLogin()
            }
        }
    }

    private fun apiCallLogin() {

        AppProgressBar.showLoaderDialog(this@Login)
        ApiClient.apiService.login(
            binding.edtEmail.text.toString().trim(),
            binding.edtPassword.text.toString().trim(),
        ).enqueue(object :
            Callback<ModelLogin> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelLogin>,
                response: Response<ModelLogin>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Login, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@Login, "Unauthorized")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        sessionManager.isLogin = true
                        sessionManager.idToken = "Bearer " + response.body()!!.token
                        sessionManager.userId = response.body()!!.user_id.toString()
                        //apiCallGetProfile()

                        Handler(Looper.getMainLooper()).postDelayed({
                            AppProgressBar.hideLoaderDialog()

                            Log.e("sessionManager.idToken", sessionManager.idToken.toString())
                            Log.e("sessionManager.userId", sessionManager.userId.toString())
                             Log.e("sessionManager.group", sessionManager.group.toString())
                            myToast(this@Login, "Login Sucessfully")
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)

                        }, 300)
                    } else {
                        myToast(this@Login, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Login, "Try Again")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {
                myToast(this@Login, "Something went wrong")
                AppProgressBar.hideLoaderDialog()
                countlogin++
                if (countlogin <= 3) {
                    Log.e("count", countlogin.toString())
                    apiCallLogin()
                } else {
                    myToast(this@Login, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }
    private fun apiCallGetProfile() {

        ApiClient.apiService.getProfile(
            sessionManager.idToken.toString()
        ).enqueue(object :
            Callback<ModelMyTra> {
            @SuppressLint("LogNotTimber", "LongLogTag", "SetTextI18n")
            override fun onResponse(
                call: Call<ModelMyTra>,
                response: Response<ModelMyTra>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        sessionManager.usertype = response.body()!!.type
                        sessionManager.customerName = response.body()!!.name
                        sessionManager.phoneNumber = response.body()!!.phone
                        sessionManager.email = response.body()!!.email
                        sessionManager.profilePic = response.body()!!.applogo
                    } else {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelMyTra>, t: Throwable) {
                AppProgressBar.hideLoaderDialog()
                count++
                if (count <= 3) {
                    Log.e("count", count.toString())
                    apiCallGetProfile()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }


    override fun onStart() {
        super.onStart()
        if (isOnline(context)) {
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()

        }
    }
}