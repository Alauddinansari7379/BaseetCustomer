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
import com.amtech.baseetcustomer.databinding.ActivityCarRentalBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class CarRental : AppCompatActivity() {
    private val binding by lazy { ActivityCarRentalBinding.inflate(layoutInflater) }
    private val context = this@CarRental
    lateinit var sessionManager: SessionManager
    private var countryList = ArrayList<ModelSpinner>()
    private var carTypeList = ArrayList<ModelSpinner>()
    private var travellingList = ArrayList<ModelSpinner>()
    private var startTimeList = ArrayList<ModelSpinner>()
    var country = ""
    var cartype = ""
    private var drivingType = ""
    var travelPerson = ""
    var startTime = ""
    var endTime = ""
    var count=0
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


            carTypeList.add(ModelSpinner("Sedan", "1"))
            carTypeList.add(ModelSpinner("Coupe", "1"))
            carTypeList.add(ModelSpinner("Wagon", "1"))
            carTypeList.add(ModelSpinner("Hatchback", "1"))
            carTypeList.add(ModelSpinner("Off-road", "1"))
            carTypeList.add(ModelSpinner("SUV", "1"))
            carTypeList.add(ModelSpinner("Pickup", "1"))
            carTypeList.add(ModelSpinner("Cabriolet", "1"))
            carTypeList.add(ModelSpinner("Limbo", "1"))
            carTypeList.add(ModelSpinner("Minivan", "1"))

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


            spinnerCarType.adapter = ArrayAdapter<ModelSpinner>(
                context, R.layout.spinner_layout_big, carTypeList
            )

            spinnerCarType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (carTypeList.size > 0) {
                        cartype = carTypeList[i].text
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }

            radioSelf.setOnCheckedChangeListener { _, _ ->
                if (radioSelf.isChecked) {
                    drivingType = "Self-Driving"
                    radioDriver.isChecked = false


                }
            }
            radioDriver.setOnCheckedChangeListener { _, _ ->
                if (radioDriver.isChecked) {
                    drivingType = "Driver"
                    radioSelf.isChecked = false
                }
            }
            for (i in 1..7) {
                travellingList.add(ModelSpinner(i.toString(), "1"))
            }
            for (i in 1..24) {
                startTimeList.add(ModelSpinner(i.toString()+":00", "1"))
            }
            spinnerTravelling.adapter =
                ArrayAdapter<ModelSpinner>(
                    context,
                    R.layout.spinner_layout_big,
                    travellingList
                )

            spinnerTravelling.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        i: Int,
                        l: Long
                    ) {
                        if (travellingList.size > 0) {
                            travelPerson = travellingList[i].text
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
            btnSubmit.setOnClickListener {
                if (edtName.text.toString().isEmpty()) {
                    edtName.error = "Enter Name"
                    edtName.requestFocus()
                    return@setOnClickListener
                }
                if (edtComment.text.toString().isEmpty()) {
                    edtComment.error = "Enter Description"
                    edtComment.requestFocus()
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
        ApiClient.apiService.requestCar(
            sessionManager.idToken.toString(),
            binding.edtName.text.toString(),
            binding.edtComment.text.toString().trim(),
            cartype,
            drivingType,
            travelPerson,
            multipleSelectedDate.toString(),
            binding.edtPrice.text.toString().trim(),
            startTime,
            endTime,
            country,
            "car"
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