package com.amtech.baseetcustomer.MainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.amtech.baseetcustomer.AddService.CarRental
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterCar
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.car
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.statisticsList
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentCarBinding
import com.amtech.baseetcustomer.sharedpreferences.SessionManager
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class CarFragment : Fragment() {
    private lateinit var binding: FragmentCarBinding
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCarBinding.bind(view)
        sessionManager= SessionManager(activity)
        activity?.let { MainActivity().languageSetting(it,sessionManager.selectedLanguage.toString()) }


        binding.btnRequest.setOnClickListener {
            startActivity(Intent(requireActivity(),CarRental::class.java))
        }
        binding.spinnerStatistics.adapter = ArrayAdapter<ModelSpinner>(
            requireContext(), R.layout.spinner_layout, statisticsList
        )


        binding.spinnerStatistics.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (statisticsList.size > 0) {
                        val statusChange = statisticsList[i].value

                        val requested = ArrayList<ModelGetTranslatorItem>()
                        val pending = ArrayList<ModelGetTranslatorItem>()
                        for (i in car) {
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
                        when (statusChange) {
                            "All Booking" -> {
                                binding.recyclerViewCar.apply {
                                    adapter = AdapterCar(requireActivity(), car)
                                }
                            }

                            "Pending" -> {
                                binding.recyclerViewCar.apply {
                                    adapter = AdapterCar(requireActivity(), pending)
                                }
                            }

                            else -> {
                                binding.recyclerViewCar.apply {
                                    adapter = AdapterCar(requireActivity(), requested)
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