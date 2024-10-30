package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.amtech.baseetcustomer.AddService.Model.ModelRequest
import com.amtech.baseetcustomer.AddService.Translator.Companion.country
import com.amtech.baseetcustomer.AddService.Translator.Companion.endTime
import com.amtech.baseetcustomer.AddService.Translator.Companion.multipleSelectedDate
import com.amtech.baseetcustomer.AddService.Translator.Companion.startTime
import com.amtech.baseetcustomer.AddService.Translator.Companion.type
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.Util
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityHomeRentalBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class HomeRental : AppCompatActivity() {
    private val binding by lazy { ActivityHomeRentalBinding.inflate(layoutInflater) }
    private val context = this@HomeRental
    lateinit var sessionManager: SessionManager
    private var countryList = ArrayList<ModelSpinner>()
    private var homeTypeList = ArrayList<ModelSpinner>()
     private var startTimeList = ArrayList<ModelSpinner>()
     private var adult = 1
    private var child = 0
    private var homeType = ""
     var count = 0
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)

        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())
      //  setLanguage(sessionManager.selectedLanguage.toString())
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
            imgBack.setOnClickListener {
                onBackPressed()
            }

            countryList.add(ModelSpinner(resources.getString(R.string.QATAR), "QATAR"))
            countryList.add(ModelSpinner(resources.getString(R.string.SAUDI_ARABIA), "SAUDI ARABIA"))
            countryList.add(ModelSpinner(resources.getString(R.string.UK), "UK"))
            countryList.add(ModelSpinner(resources.getString(R.string.US), "US"))
            countryList.add(ModelSpinner(resources.getString(R.string.UNITED_ARAB_EMIRATES), "UNITED ARAB EMIRATES"))
            countryList.add(ModelSpinner(resources.getString(R.string.TURKEY), "TURKEY"))


            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall1), "1+1(1 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall2), "2+1(2 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall3), "3+1(3 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall4), "4+1(4 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Room_Hall5), "5+1(5 Room + Hall)"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Villa), "Villa"))
            homeTypeList.add(ModelSpinner(resources.getString(R.string.Wood_Home), "Wood_Home"))

            spinnerCountry.adapter = ArrayAdapter<ModelSpinner>(
                context, R.layout.spinner_layout_big, countryList
            )

            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (countryList.size > 0) {
                        country = countryList[i].value
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }


            spinnerHomeType.adapter = ArrayAdapter<ModelSpinner>(
                context, R.layout.spinner_layout_big, homeTypeList
            )

            spinnerHomeType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (homeTypeList.size > 0) {
                        homeType = homeTypeList[i].value
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }


            for (i in 1..24) {
                startTimeList.add(ModelSpinner(i.toString() + ":00", "1"))
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

            layoutMinuAdult.setOnClickListener {
                if (adult>1){
                    adult--
                    binding.tvCountAdult.text=adult.toString()
                }

            }

            layoutPlushAdult.setOnClickListener {
                     adult++
                    binding.tvCountAdult.text=adult.toString()


            }

            layoutMinuChild.setOnClickListener {
                if (child>0){
                    child--
                    binding.tvCountChild.text=child.toString()
                }

            }

            layoutPlushChild.setOnClickListener {
                     child++
                    binding.tvCountChild.text=child.toString()


            }
            btnSubmit.setOnClickListener {
                if (edtName.text.toString().isEmpty()) {
                    edtName.error = resources.getString(R.string.Enter_Name)
                    edtName.requestFocus()
                    return@setOnClickListener
                }
                if (edtDetail.text.toString().isEmpty()) {
                    edtDetail.error = resources.getString(R.string.Enter_Detail)
                    edtDetail.requestFocus()
                    return@setOnClickListener
                }

                if (edtPrice.text.toString().isEmpty()) {
                    edtPrice.error = "Enter Price"
                    edtPrice.requestFocus()
                    return@setOnClickListener
                }
                if (multipleSelectedDate.isEmpty()) {
                    myToast(context,resources.getString(R.string.Please_select_Service_date))
                    return@setOnClickListener
                }
                val i = Intent(context, VendorList::class.java)
                    .putExtra("name", edtName.text.toString())
                    .putExtra("Description", edtDescription.text.toString())
                    .putExtra("price", edtPrice.text.toString())
                    .putExtra("type", type.toString())
                    .putExtra("carType", homeType.toString())
                    .putExtra("child", child.toString())
                    .putExtra("adult", adult.toString())
                    .putExtra("homeDetail", edtDetail.toString())
                    .putExtra("homeType", homeType.toString())
                    .putExtra("bookingType", "home")

                context.startActivity(i)
               // apiCallRequestCar()

            }

        }

        val callback = MultipleDaysPickCallback {
            multipleSelectedDate.clear()
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
    private fun setLanguage(languageCode: String) {
        Util.LocaleHelper.setLocale(this, languageCode)
        // Restart activity to apply the new locale
        val intent = Intent(this, MainActivity::class.java)
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
    private fun apiCallRequestCar() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.requestHome(
            sessionManager.idToken.toString(),
            binding.edtName.text.toString(),
            binding.edtDetail.text.toString(),
            adult.toString(),
            child.toString(),
            homeType,
            binding.edtDescription.text.toString(),
            multipleSelectedDate.toString(),
            binding.edtPrice.text.toString(),
            startTime,
            endTime,
            country,
            "home",
        ).enqueue(object : Callback<ModelRequest> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelRequest>, response: Response<ModelRequest>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, resources.getString(R.string.service_request))
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
                    apiCallRequestCar()
                } else {
                    myToast(context, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }
                myToast(context, resources.getString(R.string.Something_went_wrong))
                AppProgressBar.hideLoaderDialog()
            }

        })

    }

    private fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        val date = inputFormat.parse(inputDate)
        val formattedDate = outputFormat.format(date!!)

        return formattedDate

    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.back = true
    }
    override fun onDestroy() {
        super.onDestroy()
        MainActivity.refreshLanNew=true
    }
    override fun onStart() {
        super.onStart()
        MainActivity().languageSetting(context,sessionManager.selectedLanguage.toString())

    }
}
