package com.amtech.baseetcustomer.SignUp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.Login.Login
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.SignUp.Model.ModelSignUp
import com.amtech.baseetcustomer.SignUp.Model.handleErrorResponse
import com.amtech.baseetcustomer.databinding.ActivitySignUpBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    lateinit var sessionManager: SessionManager
    val context = this@SignUp
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
         MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())
        with(binding) {
            btnSignUp.setOnClickListener {
                if (edtFirstName.text.toString().isEmpty()) {
                    edtFirstName.error = resources.getString(R.string.Enter_First_Name)
                    edtFirstName.requestFocus()
                    return@setOnClickListener
                }
                if (edtLastName.text.toString().isEmpty()) {
                    edtLastName.error = resources.getString(R.string.Enter_Last_Name)
                    edtLastName.requestFocus()
                    return@setOnClickListener
                }
                if (edtEmail.text.toString().isEmpty()) {
                    edtEmail.error = resources.getString(R.string.Enter_Email)
                    edtEmail.requestFocus()
                    return@setOnClickListener
                }
                if (edtPhone.text.toString().isEmpty()) {
                    edtPhone.error =  resources.getString(R.string.enter_phone_number)
                    edtPhone.requestFocus()
                    return@setOnClickListener
                }
                if (edtPhone.text!!.length < 10) {
                    myToast(context,  resources.getString(R.string.Enter_valid_phone_number))
                    edtPhone.requestFocus()
                    return@setOnClickListener
                }
                if (edtPassword.text.toString().isEmpty()) {
                    edtPassword.error =  resources.getString(R.string.Enter_Password)
                    edtPassword.requestFocus()
                    return@setOnClickListener
                }
                if (edtPassword.text!!.length < 6) {
                    myToast(context,  resources.getString(R.string.password_is_to_short))
                    edtPassword.requestFocus()
                    return@setOnClickListener
                }
                apiCallSignUp()
            }

        }
    }

    private fun apiCallSignUp() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.register(
            binding.edtFirstName.text.toString().trim(),
            binding.edtLastName.text.toString().trim(),
            binding.edtEmail.text.toString().trim(),
            binding.edtPhone.text.toString().trim(),
            binding.edtPassword.text.toString().trim(),
        ).enqueue(object :
            Callback<ModelSignUp> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelSignUp>,
                response: Response<ModelSignUp>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context,  resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context,  resources.getString(R.string.Unauthorized))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 403) {
                        myToast(context,resources.getString(R.string.The_email_phone_has_already_been_taken))

                       /* val jsonResponse ="""${response.body()}"""
                        Log.e("Response",response.body().toString())
                        // Parse the JSON response
                        val jsonObject = JSONObject(jsonResponse)
                        val errorsArray: JSONArray = jsonObject.getJSONArray("errors")

                        // Extract and print the error messages
                        for (i in 0 until errorsArray.length()) {
                            val errorObject: JSONObject = errorsArray.getJSONObject(i)
                            val message: String = errorObject.getString("message")
                            println(message)
                            myToast(context, message)

                        }*/
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        AppProgressBar.hideLoaderDialog()
                        myToast(context, resources.getString(R.string.Register_Sucessfully))
                        val intent = Intent(applicationContext, Login::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Try_Again))
                    AppProgressBar.hideLoaderDialog()

                }
            }

            override fun onFailure(call: Call<ModelSignUp>, t: Throwable) {
                AppProgressBar.hideLoaderDialog()
                count++
                if (count <= 3) {
                    Log.e("count", count.toString())
                    apiCallSignUp()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                }
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

}


