package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.amtech.baseetcustomer.AddService.Model.ModelRequest
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.ImageUploadClass.UploadRequestBody
import com.amtech.baseetcustomer.Helper.Util
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.refreshLan
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.refreshLanNew
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityTranslatorBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import kotlinx.coroutines.DelicateCoroutinesApi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class Translator : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private val binding by lazy {
        ActivityTranslatorBinding.inflate(layoutInflater)
    }
    var trFromList = ArrayList<ModelSpinner>()
    private var travellingList = ArrayList<ModelSpinner>()
    private var startTimeList = ArrayList<ModelSpinner>()
    private var serviceHour = ArrayList<ModelSpinner>()
    private var countryList = ArrayList<ModelSpinner>()
    private var multipleSelectedDate = StringBuilder()

    var count = 0
    var type = "On Call"
    var translationFrom = ""
    var translationTo = ""
    var serviceHourNew = ""
    var startTime = ""
    var endTime = ""
    var country = ""
    private var selectedImageUri: Uri? = null
    val context = this@Translator
    lateinit var sessionManager: SessionManager

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        //  setLanguage(sessionManager.selectedLanguage.toString())

//        GlobalScope.launch {
//            delay(1000L)
//
//        }
//         Thread.sleep(2000L)
        MainActivity().languageSetting(context, sessionManager.selectedLanguage.toString())

        if (refreshLanNew) {
            refreshLanNew = false
            refresh()
        }


        // languageSetting(sessionManager.selectedLanguage.toString())
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            trFromList.add(ModelSpinner(resources.getString(R.string.Turkish), "Turkish"))
            trFromList.add(ModelSpinner(resources.getString(R.string.Arabic), "Arabic"))
            trFromList.add(ModelSpinner(resources.getString(R.string.English), "English"))
            trFromList.add(ModelSpinner(resources.getString(R.string.Russian), "Russian"))


            countryList.add(ModelSpinner(resources.getString(R.string.QATAR), "QATAR"))
            countryList.add(
                ModelSpinner(
                    resources.getString(R.string.SAUDI_ARABIA),
                    "SAUDI ARABIA"
                )
            )
            countryList.add(ModelSpinner(resources.getString(R.string.UK), "UK"))
            countryList.add(ModelSpinner(resources.getString(R.string.US), "US"))
            countryList.add(
                ModelSpinner(
                    resources.getString(R.string.UNITED_ARAB_EMIRATES),
                    "UNITED ARAB EMIRATES"
                )
            )
            countryList.add(ModelSpinner(resources.getString(R.string.TURKEY), "TURKEY"))

            //  ServiceDate.text = currentDate

            spinnerFrom.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    trFromList
                )

            spinnerFrom.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        val locale = Locale("en")
                        Locale.setDefault(locale)
                        val resources: Resources = context.resources
                        val config: Configuration = resources.configuration
                        config.setLocale(locale)
                        resources.updateConfiguration(config, resources.displayMetrics)

                        if (trFromList.size > 0) {
                            translationFrom = trFromList[i].value
                            Log.e("translationFrom", translationFrom)
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerCountry.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    countryList
                )

            spinnerCountry.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (countryList.size > 0) {
                            country = countryList[i].value
                            Log.e("country", country)

                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerTo.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    trFromList
                )

            spinnerTo.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (trFromList.size > 0) {
                            translationTo = trFromList[i].value
                            Log.e("translationTo", translationTo)

                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            for (i in 1..12) {
                serviceHour.add(ModelSpinner(i.toString(), "1"))
            }

            for (i in 1..7) {
                travellingList.add(ModelSpinner(i.toString(), "1"))
            }

            for (i in 1..24) {
                startTimeList.add(ModelSpinner(i.toString() + ":00", "1"))
            }

            spinnerServiceH.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    serviceHour
                )

            spinnerServiceH.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (serviceHour.size > 0) {
                            serviceHourNew = serviceHour[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerStartT.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    startTimeList
                )

            spinnerStartT.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (startTimeList.size > 0) {
                            startTime = startTimeList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            spinnerEndT.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    startTimeList
                )

            spinnerEndT.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (startTimeList.size > 0) {
                            endTime = startTimeList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }

            radioOnCall.setOnCheckedChangeListener { _, _ ->
                if (radioOnCall.isChecked) {
                    type = "On Call"
                    radioInPerson.isChecked = false
                    radioDoc.isChecked = false
                    layoutServiceDate.visibility = View.GONE

                }

            }
            radioInPerson.setOnCheckedChangeListener { _, _ ->
                if (radioInPerson.isChecked) {
                    type = "In Person"
                    radioOnCall.isChecked = false
                    radioDoc.isChecked = false
                    layoutServiceDate.visibility = View.GONE
                }
            }
            radioDoc.setOnCheckedChangeListener { _, _ ->
                if (radioDoc.isChecked) {
                    type = "Doc"
                    ServiceDate.text = ""
                    radioOnCall.isChecked = false
                    radioInPerson.isChecked = false
                    layoutServiceDate.visibility = View.VISIBLE
                }
            }
            btnChoose.setOnClickListener {
                openImageChooser()
            }

            btnSubmit.setOnClickListener {
                if (edtName.text.toString().isEmpty()) {
                    edtName.error = resources.getString(R.string.Enter_Name)
                    edtName.requestFocus()
                    return@setOnClickListener
                }
                if (edtDescription.text.toString().isEmpty()) {
                    edtDescription.error = resources.getString(R.string.Enter_Description)
                    edtDescription.requestFocus()
                    return@setOnClickListener
                }

                if (edtPrice.text.toString().isEmpty()) {
                    edtPrice.error = resources.getString(R.string.Enter_Price)
                    edtPrice.requestFocus()
                    return@setOnClickListener
                }
                if (multipleSelectedDate.isEmpty()) {
                    myToast(context, resources.getString(R.string.Please_select_Service_date))
                    return@setOnClickListener
                }
                if (selectedImageUri == null) {
                    requestTraWithoutImage()
                } else {
                    apiCallRequestTra()

                }

            }


        }
        val callback = MultipleDaysPickCallback {
            multipleSelectedDate.clear()
//            val locale1 = Locale("en")
//            Locale.setDefault(locale1)
//            val resources1: Resources = context.resources
//            val config1: Configuration = resources.configuration
//            config1.setLocale(locale1)
//            resources.updateConfiguration(config1, resources1.displayMetrics)

            for (i in it) {
                val conDate = convertDate(i.longDateString)
                multipleSelectedDate.append("${conDate},")
                Log.e("multipleSelectedDate", multipleSelectedDate.toString())
            }
            binding.ServiceDate.text = multipleSelectedDate.toString()
            val locale = Locale(sessionManager.selectedLanguage!!)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

        }
        binding.ServiceDate.setOnClickListener {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            val today = CivilCalendar()
            val datePicker = PrimeDatePicker.dialogWith(today)
                .pickMultipleDays(callback)
                .build()
            datePicker.show(supportFragmentManager, "MultipleDatePicker")


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        refreshLanNew=true
    }

    private fun setLanguage(languageCode: String) {
        Util.LocaleHelper.setLocale(this, languageCode)
        // Restart activity to apply the new locale
        val intent = Intent(this, Translator::class.java)
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

    private fun apiCallRequestTra() {
        if (selectedImageUri == null) {
            myToast(context, resources.getString(R.string.Please_Select_Payment_Type))
            return
        }
        AppProgressBar.showLoaderDialog(context)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.requestTra(
            sessionManager.idToken.toString(),
            binding.edtName.text.toString().trim(),
            binding.edtDescription.text.toString().trim(),
            type,
            translationFrom,
            translationTo,
            serviceHourNew,
            multipleSelectedDate.toString(),
            binding.edtPrice.text.toString().trim(),
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
                    apiCallRequestTra()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                myToast(context, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun requestTraWithoutImage() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.requestTraWithoutImage(
            sessionManager.idToken.toString(),
            binding.edtName.text.toString().trim(),
            binding.edtDescription.text.toString().trim(),
            type,
            translationFrom,
            translationTo,
            serviceHourNew,
            multipleSelectedDate.toString(),
            binding.edtPrice.text.toString().trim(),
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
                    requestTraWithoutImage()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                myToast(context, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
//
//        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pdfIntent.type = "application/pdf"
//        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
//                    binding!!.tvChoice.setTextColor(Color.parseColor("#FF4CAF50"))
//                    binding!!.tvChoice.text = "Image Selected"

                    //binding.imageViewNew.visibility = View.VISIBLE
                    binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(inputDate)
        val formattedDate = outputFormat.format(date!!)

        return formattedDate

    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
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

    override fun onProgressUpdate(percentage: Int) {

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.back = true
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

}