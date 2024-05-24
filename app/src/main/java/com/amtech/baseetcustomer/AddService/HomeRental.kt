package com.amtech.baseetcustomer.AddService

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.amtech.baseetcustomer.Helper.myToast
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
    var country = ""
    private var adult = 1
    private var child = 0
    private var homeType = ""
     var startTime = ""
    var endTime = ""
    var count = 0
    private var multipleSelectedDate = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(context)
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            countryList.add(ModelSpinner("QATAR", "1"))
            countryList.add(ModelSpinner("SAUDI ARABIA", "1"))
            countryList.add(ModelSpinner("UK", "1"))
            countryList.add(ModelSpinner("US", "1"))
            countryList.add(ModelSpinner("UNITED ARAB EMIRATES", "1"))
            countryList.add(ModelSpinner("TURKEY", "1"))


            homeTypeList.add(ModelSpinner("1+1(1 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("2+1(2 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("3+1(3 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("4+1(4 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("5+1(5 Room + Hall)", "1"))
            homeTypeList.add(ModelSpinner("Villa", "1"))
            homeTypeList.add(ModelSpinner("Wood Home", "1"))

            spinnerCountry.adapter = ArrayAdapter<ModelSpinner>(
                context, R.layout.spinner_layout_big, countryList
            )

            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (countryList.size > 0) {
                        country = countryList[i].text
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
                        homeType = homeTypeList[i].text
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
                    edtName.error = "Enter Name"
                    edtName.requestFocus()
                    return@setOnClickListener
                }
                if (edtDetail.text.toString().isEmpty()) {
                    edtDetail.error = "Enter Detail"
                    edtDetail.requestFocus()
                    return@setOnClickListener
                }

                if (edtPrice.text.toString().isEmpty()) {
                    edtPrice.error = "Enter Price"
                    edtPrice.requestFocus()
                    return@setOnClickListener
                }
                apiCallRequestCar()

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

        }
        binding.ServiceDate.setOnClickListener {
            val today = CivilCalendar()
            val datePicker = PrimeDatePicker.dialogWith(today)
                .pickMultipleDays(callback)
                .build()
            datePicker.show(supportFragmentManager, "MultipleDatePicker")
        }
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
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, "Something went wrong")
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
                    myToast(context, "Something went wrong")
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
                myToast(context, "Something went wrong")
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
}
