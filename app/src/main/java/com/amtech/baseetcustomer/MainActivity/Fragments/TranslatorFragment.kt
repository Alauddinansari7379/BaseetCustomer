package com.amtech.baseetcustomer.MainActivity.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.amtech.baseetcustomer.AddService.Translator
import com.amtech.baseetcustomer.Helper.AppProgressBar
import com.amtech.baseetcustomer.Helper.myToast
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterCar
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterTranslator
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.back
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.car
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.home
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.statisticsList
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.translator
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslator
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentTranslatorBinding
import com.amtech.baseetcustomer.retrofit.ApiClient
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranslatorFragment : Fragment() {
    private lateinit var binding: FragmentTranslatorBinding
    private lateinit var sessionManager: SessionManager
    var count = 0
    var mainData = ArrayList<ModelGetTranslatorItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_translator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTranslatorBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        with(binding) {
            apiCallGetRequest()

            btnRequest.setOnClickListener {
                startActivity(Intent(requireContext(), Translator::class.java))
            }

            binding.recyclerView.apply {
                adapter = AdapterTranslator(requireContext(), mainData)
            }

            spinnerStatistics.adapter = ArrayAdapter<ModelSpinner>(
                requireContext(), R.layout.spinner_layout, statisticsList
            )

            spinnerStatistics.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?, view: View?, i: Int, l: Long
                    ) {
                        if (statisticsList.size > 0) {
                            val statusChange = statisticsList[i].text

                            val requested = ArrayList<ModelGetTranslatorItem>()
                            val pending = ArrayList<ModelGetTranslatorItem>()
                            for (i in translator) {
                                val jsonArray = JsonArray()
                                jsonArray.add("0")
                                val jsonObject = JsonObject()
                                jsonObject.add("accept_by", jsonArray)

                                val jsonString = "${i.accept_by}"

                                println(jsonString)
                                Log.e("accept_by",jsonString)
                                if (jsonString.contains("0")) {
                                    requested.add(i)
                                } else {
                                    pending.add(i)
                                }

                            }
                            when (statusChange){
                               "All Booking"-> {
                                   binding.recyclerView.apply {
                                       adapter = AdapterTranslator(requireContext(), translator)
                                   }
                               }
                                "Pending"->{
                                    binding.recyclerView.apply {
                                        adapter = AdapterTranslator(requireContext(), pending)
                                    }
                                }else->{
                                binding.recyclerView.apply {
                                    adapter = AdapterTranslator(requireContext(), requested)
                                }
                                }

                            }
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                }
        }


    }

    private fun apiCallGetRequest() {
        AppProgressBar.showLoaderDialog(requireActivity())
        AppProgressBar.showLoaderDialog(requireActivity())
        ApiClient.apiService.getRequest(
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelGetTranslator> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetTranslator>, response: Response<ModelGetTranslator>
                ) {
                    try {
                        if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.isEmpty()) {
                            myToast(requireActivity(), "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            mainData = response.body()!!
                            car.clear()
                            home.clear()
                            translator.clear()

                            for (i in mainData) {
                                if (i.food_type != null) {
                                    when (i.food_type) {
                                        "car" -> car.add(i)
                                        "home" -> home.add(i)
                                        "translator" -> translator.add(i)
                                    }

                                }

                            }
                            Log.e("mainData.size", mainData.size.toString())
                            Log.e("car.size", car.size.toString())
                            Log.e("home.size", home.size.toString())
                            Log.e("translator.size", translator.size.toString())
                            binding.recyclerView.apply {
                                adapter = AdapterTranslator(requireContext(), translator)
                            }

                            val recyclerViewCar =
                                (requireActivity().findViewById<View>(R.id.recyclerViewCar) as RecyclerView)

                            recyclerViewCar.apply {
                                adapter = AdapterCar(requireContext(), car)
                            }

                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(requireActivity(), "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelGetTranslator>, t: Throwable) {
                    myToast(requireActivity(), t.message.toString())
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallGetRequest()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()
                    }
                    AppProgressBar.hideLoaderDialog()
                }
            })
    }
    override fun onResume() {
        super.onResume()
        if (back){
            back=false
            apiCallGetRequest()
        }
    }
}