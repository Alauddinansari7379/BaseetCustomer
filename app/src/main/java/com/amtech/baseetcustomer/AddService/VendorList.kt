package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amtech.baseetcustomer.AddService.Model.ModelRequest
import com.amtech.baseetcustomer.AddService.Model.ModelVendorList
import com.amtech.baseetcustomer.AddService.Translator.Companion.bookingType
import com.amtech.baseetcustomer.AddService.Translator.Companion.country
import com.amtech.baseetcustomer.AddService.Translator.Companion.endTime
import com.amtech.baseetcustomer.AddService.Translator.Companion.multipleSelectedDate
import com.amtech.baseetcustomer.AddService.Translator.Companion.selectedImageUri
import com.amtech.baseetcustomer.AddService.Translator.Companion.serviceHourNew
import com.amtech.baseetcustomer.AddService.Translator.Companion.startTime
import com.amtech.baseetcustomer.AddService.Translator.Companion.translationFrom
import com.amtech.baseetcustomer.AddService.Translator.Companion.translationTo
import com.amtech.baseetcustomer.AddService.modeldetails.Data
import com.amtech.baseetcustomer.AddService.modeldetails.ModelDetails
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.ImageUploadClass.UploadRequestBody
import com.amtech.baseetcustomer.Helper.Util
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterVendorList
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.refreshLanNew
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.AcitvityVendorListBinding
import com.amtech.baseetcustomer.databinding.PopupServicDetailsBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale
import java.util.concurrent.TimeUnit


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
    var adult = ""
    var child = ""
    private var allServiceDetails: Data? = null
    var names = ""
    var homeDetail = ""
    private var carType = ""
    private var homeType = ""
    private var travelPerson = ""
    private var serviceDate = ""
    val context = this@VendorList
    var data = 0
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        description = intent.getStringExtra("name").toString()
        name = intent.getStringExtra("name").toString()
        price = intent.getStringExtra("price").toString()
        type = intent.getStringExtra("type").toString()
        carType = intent.getStringExtra("carType").toString()
        travelPerson = intent.getStringExtra("travelPerson").toString()
        bookingType = intent.getStringExtra("bookingType").toString()
        child = intent.getStringExtra("child").toString()
        adult = intent.getStringExtra("adult").toString()
        homeDetail = intent.getStringExtra("homeDetail").toString()
        homeType = intent.getStringExtra("homeType").toString()


        if (Picasso.get() == null) {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val picassoInstance = Picasso.Builder(this)
                .downloader(OkHttp3Downloader(okHttpClient))
                .build()

            Picasso.setSingletonInstance(picassoInstance)
        }


        with(binding) {
            tvName.text = name
            tvPrice.text = price
            tvLangauge.text = translationFrom + " to " + translationTo
            tvCountry.text = "Country :" + country
            tvTime.text = startTime + " to " + endTime
            tvType.text = type
            tvDates.text = multipleSelectedDate

            serviceDate = multipleSelectedDate.toString()
            if (bookingType == "car") {

                layoutTraveling.visibility = View.VISIBLE
                tvLangauge.visibility = View.GONE
                layoutType.visibility = View.GONE
                tvTravlingPer.text = travelPerson
                tvCarType.text = carType

                translationFrom = ""
                translationTo = ""
                serviceHourNew = ""
                type = ""
                serviceDate = ""

            }


            if (bookingType == "home") {
                serviceDate = ""
                layoutFamilyPer.visibility = View.VISIBLE
                tvLangauge.visibility = View.GONE
                layoutType.visibility = View.GONE
                tvTravlingPer.text = travelPerson
                tvCarType.text = carType
                tvCountChild.text = child
                tvCountAdult.text = adult
                tvHomeDetail.text = homeDetail
                tvHomeType.text = homeType
                translationFrom = ""
                translationTo = ""
                serviceHourNew = ""
                type = ""
            }
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
//            imgBack.setOnClickListener {
//                onBackPressed()
//            }

        }
        apiCallVendorList()


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




    private fun apiCallVendorList() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.fetchService(
            sessionManager.idToken.toString(), bookingType, price, "",
            "", "", "", type

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
                            data = response.body()!!.data.size
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

    fun apiCallGetDetails(typeFood: String, id: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getDetails(sessionManager.idToken.toString(), id)
            .enqueue(object : Callback<ModelDetails> {

                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<ModelDetails>,
                    response: Response<ModelDetails>
                ) {
                    try {
                        when (response.code()) {
                            404 -> myToast(context, resources.getString(R.string.Something_went_wrong))
                            500 -> myToast(context, resources.getString(R.string.Server_Error))
                            else -> {
                                count = 0
                                allServiceDetails = response.body()?.data

                                val binding = PopupServicDetailsBinding.inflate(LayoutInflater.from(context))

                                allServiceDetails?.let { details ->
                                    with(binding) {
                                        tvName.text = details.name
                                        tvPrice.text = details.price.toString()

                                        if (typeFood == "translator") {
                                            tvFrom.text = "Tr from : ${details.tr_from}"
                                            tvTo.text = "Tr to : ${details.tr_to}"
                                        } else {
                                            tvFrom.visibility = View.GONE
                                            tvTo.visibility = View.GONE
                                        }

                                        tvType.text = "Type : ${details.food_type}"
                                        tvDescription.text = details.description
                                        tvTax.text = "Tax : ${details.tax}"
                                        tvStatus.text = "Status : ${details.status}"
                                        tvOrderCount.text = "Order count : ${details.order_count}"
                                        tvAvgRating.text = "Avg rating : ${details.avg_rating}"
                                        tvRatingCount.text = "Rating count : ${details.rating_count}"
                                        tvQty.text = "Qty : ${details.qty}"

                                        if (typeFood == "car") {
                                            tvCarType.text = details.car_type.toString()
                                            tvTravlingPer.text = details.trperson.toString()
                                            tvDatesN.text = "Dates : ${details.dates}"
                                            tvHomeDays.text = "Days : ${details.home_days}"
                                        } else {
                                            tvCarType.visibility = View.GONE
                                            tvTravlingPer.visibility = View.GONE
                                            tvDatesN.visibility = View.GONE
                                            tvHomeDays.visibility = View.GONE
                                            tvTravalPerson.visibility = View.GONE
                                            tvCartypeHN.visibility = View.GONE
                                        }

                                        if (typeFood == "home") {
                                            tvHomeDetail.text = "Home details : ${details.amenities}"
                                            tvHomeType.text = "Home type : ${details.car_type}"
                                        } else {
                                            tvHomeDetail.visibility = View.GONE
                                            tvHomeType.visibility = View.GONE
                                        }

//                                        Glide.with(context)
//                                            .load(details.appimage)
//                                            .into(ivImage)

//                                        Picasso.get().load(details.appimage )
//                                            .placeholder(R.drawable.image)
//                                            .error(R.drawable.no_image_available)
//                                            .into(binding.ivImage)
                                        var appimage=""
                                        if (!appimage.isNullOrEmpty()) {
                                         appimage = details.appimage!!.replace("http", "https")

                                            Picasso.get()
                                                .load(appimage)
                                                .placeholder(R.drawable.image) // This is the placeholder that will be set in onPrepareLoad
                                                .error(R.drawable.no_image_available)
                                                .resize(500, 500) // Resize dimensions as needed
                                                .centerCrop()
                                                .into(object : Target {
                                                    override fun onBitmapLoaded(
                                                        bitmap: Bitmap?,
                                                        from: Picasso.LoadedFrom?
                                                    ) {
                                                        Log.d(
                                                            "Picasso",
                                                            "Bitmap loaded successfully"
                                                        )
                                                        binding.ivImage.setImageBitmap(bitmap)
                                                    }

                                                    override fun onBitmapFailed(
                                                        e: Exception?,
                                                        errorDrawable: Drawable?
                                                    ) {
                                                        Log.e("Picasso", "Bitmap load failed", e)
                                                        binding.ivImage.setImageDrawable(
                                                            errorDrawable
                                                        )
                                                    }

                                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                                        Log.d("Picasso", "Preparing to load bitmap")
                                                        if (placeHolderDrawable != null) {
                                                            binding.ivImage.setImageDrawable(
                                                                placeHolderDrawable
                                                            )
                                                        } else {
                                                            Log.d("Picasso", "Placeholder drawable is null")
                                                        }
                                                    }
                                                })
                                        }






                                        val dialog = AlertDialog.Builder(context)
                                            .setView(root)
                                            .create()

                                        imgClose.setOnClickListener { dialog.dismiss() }
                                        btnClose.setOnClickListener { dialog.dismiss() }

                                        dialog.show()
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        myToast(context, resources.getString(R.string.Something_went_wrong))
                        e.printStackTrace()
                    } finally {
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelDetails>, t: Throwable) {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        apiCallGetDetails(typeFood, id)
                    } else {
                        myToast(context, t.message.toString())
                    }
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
                        //  onBackPressed()

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
            bookingType
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
                        //  onBackPressed()

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

    @SuppressLint("SetTextI18n")
    override fun showDetailsPopup(venId: String, whcSerId: String) {
        apiCallGetDetails(venId, whcSerId)

    }

    override fun onProgressUpdate(percentage: Int) {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (data != 0) {
            val i = Intent(context, MainActivity::class.java)
            context.startActivity(i)
            finish()
        }

    }

}