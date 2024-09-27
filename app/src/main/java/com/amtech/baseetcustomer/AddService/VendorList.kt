package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.baseetcustomer.AddService.Model.ModelRequest
import com.amtech.baseetcustomer.AddService.Model.ModelVendorList
import com.amtech.baseetcustomer.AddService.Translator.Companion
import com.amtech.baseetcustomer.AddService.Translator.Companion.country
import com.amtech.baseetcustomer.AddService.Translator.Companion.endTime
import com.amtech.baseetcustomer.AddService.Translator.Companion.multipleSelectedDate
import com.amtech.baseetcustomer.AddService.Translator.Companion.selectedImageUri
import com.amtech.baseetcustomer.AddService.Translator.Companion.serviceHourNew
import com.amtech.baseetcustomer.AddService.Translator.Companion.startTime
import com.amtech.baseetcustomer.AddService.Translator.Companion.translationFrom
import com.amtech.baseetcustomer.AddService.Translator.Companion.translationTo
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.ImageUploadClass.UploadRequestBody
import com.amtech.baseetcustomer.Helper.Util
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterVendorList
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.refreshLanNew
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.AcitvityVendorListBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale

class VendorList : AppCompatActivity(), AdapterVendorList.SendService,
    UploadRequestBody.UploadCallback {
    private val binding by lazy {
        AcitvityVendorListBinding.inflate(layoutInflater)
    }
     var dialog: Dialog? = null
    var count = 0
    var type = ""
     var price = ""
    var description = ""
     var name = ""
    val context = this@VendorList
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        description = intent.getStringExtra("name").toString()
        name = intent.getStringExtra("name").toString()
        price = intent.getStringExtra("price").toString()
        type = intent.getStringExtra("type").toString()

        with(binding){
            tvName.text=name
            tvPrice.text=price
            tvLangauge.text=translationFrom+" to " + translationTo
            tvCountry.text="Country :"+country
            tvTime.text= startTime+" to "+ endTime
            tvType.text= type
            tvDates.text= multipleSelectedDate
        }
        MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
        if (MainActivity.refreshLanNew) {
            MainActivity.refreshLanNew = false
            refresh()
        }
        if (sessionManager.selectedLanguage == "en") {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.arabic_text)
        } else {
            binding.imgLan.background = ContextCompat.getDrawable(context, R.drawable.english_text)
        }
        apiCallVendorList()
        binding.imgLan.setOnClickListener {
            if (sessionManager.selectedLanguage == "en") {
                sessionManager.selectedLanguage = "ar"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                sessionManager.selectedLanguage = "en"
                MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())
                overridePendingTransition(0, 0)
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }



        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        refreshLanNew = true
    }

    private fun setLanguage(languageCode: String) {
        Util.LocaleHelper.setLocale(this, languageCode)
        // Restart activity to apply the new locale
        val intent = Intent(this, VendorList::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.back = true
    }

    private fun apiCallVendorList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.fetchService(
            sessionManager.idToken.toString(), "Translator", price, translationFrom,
            translationTo, multipleSelectedDate.toString(),serviceHourNew,type
        )
            .enqueue(object : Callback<ModelVendorList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelVendorList>, response: Response<ModelVendorList>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(context, resources.getString(R.string.Something_went_wrong))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 500) {
                            myToast(context, resources.getString(R.string.Server_Error))
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.data.isEmpty()) {
                            myToast(context, resources.getString(R.string.No_Data_Found))
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerView.apply {
                                adapter = AdapterVendorList(
                                    context,
                                    response.body()!!.data,
                                    this@VendorList
                                )

                            }
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelVendorList>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallVendorList()
                    } else {
                        myToast(context, t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    fun languageSetting(languageCode: String) {
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

    fun apiCallRequestTra(id: String, whcSerId: String) {
        if (selectedImageUri == null) {
            myToast(this.context, resources.getString(R.string.Please_Select_Payment_Type))
            return
        }
        sessionManager = SessionManager(context)
        AppProgressBar.showLoaderDialog(this.context)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.requestTra(
            sessionManager.idToken.toString(),
            id,
            whcSerId,
            name,
            description,
            type,
            translationFrom,
            translationTo,
            serviceHourNew,
            multipleSelectedDate.toString(),
            price,
            startTime,
            endTime,
            country,
            "translator",
            MultipartBody.Part.createFormData("document", file.name, body),
        ).enqueue(object : Callback<ModelRequest> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelRequest>, response: Response<ModelRequest>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        onBackPressed()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelRequest>, t: Throwable) {
                count++
                if (count <= 3) {
                    Log.e("count", count.toString())
                    apiCallRequestTra(id, whcSerId)
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                myToast(context, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    fun requestTraWithoutImage(id: String, whcSerId: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.requestTraWithoutImage(
            sessionManager.idToken.toString(),
            id,
            whcSerId,
            name,
            description,
            type,
            translationFrom,
            translationTo,
            serviceHourNew,
            multipleSelectedDate.toString(),
            price,
            startTime,
            endTime,
            country,
            "translator"
        ).enqueue(object : Callback<ModelRequest> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelRequest>, response: Response<ModelRequest>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.Server_Error))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 200) {
                        myToast(context, "${response.body()!!.message}")
                        Log.d("response", response.body()!!.toString())
                        AppProgressBar.hideLoaderDialog()
                        onBackPressed()

                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, resources.getString(R.string.Something_went_wrong))
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelRequest>, t: Throwable) {
                count++
                if (count <= 3) {
                    Log.e("count", count.toString())
                    requestTraWithoutImage(id, whcSerId)
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                myToast(context, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
            }

        })

    }


    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()

        }

        return name
    }

    override fun sendService(id: String, whcSerId: String) {
        if (selectedImageUri != null) {
            apiCallRequestTra(id, whcSerId)
        } else {
            requestTraWithoutImage(id, whcSerId)
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        TODO("Not yet implemented")
    }

}