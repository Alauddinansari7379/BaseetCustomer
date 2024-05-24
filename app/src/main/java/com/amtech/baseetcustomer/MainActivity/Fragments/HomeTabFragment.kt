package com.amtech.baseetcustomer.MainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.amtech.baseetcustomer.AddService.BookingDetail
import com.amtech.baseetcustomer.AddService.HomeRental
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.AddService.Translator
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterCar
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterHome
import com.amtech.baseetcustomer.MainActivity.Adapter.AdapterTranslator
import com.amtech.baseetcustomer.MainActivity.MainActivity
import com.amtech.baseetcustomer.MainActivity.MainActivity.Companion.statisticsList
import com.amtech.baseetcustomer.MainActivity.Model.ModelGetTranslatorItem
import com.amtech.baseetcustomer.databinding.FragmentHomeTabBinding
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class HomeTabFragment : Fragment() {
    private lateinit var  binding: FragmentHomeTabBinding
    var mainData = ArrayList<ModelGetTranslatorItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeTabBinding.bind(view)
        binding.btnRequest.setOnClickListener {
            startActivity(Intent(requireContext(), HomeRental::class.java))
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
                        val statusChange = statisticsList[i].text

                        val requested = ArrayList<ModelGetTranslatorItem>()
                        val pending = ArrayList<ModelGetTranslatorItem>()
                        for (i in MainActivity.home) {
                            val jsonArray = JsonArray()
                            jsonArray.add("0")
                            val jsonObject = JsonObject()
                            jsonObject.add("accept_by", jsonArray)

                            val jsonString = "${i.accept_by}"

                            println(jsonString)
                            Log.e("accept_by", jsonString)
                            if (jsonString.contains("0")) {
                                requested.add(i)
                            } else {
                                pending.add(i)
                            }
                        }
                        when (statusChange) {
                            "All Booking" -> {
                                binding.recyclerView.apply {
                                    adapter = AdapterHome(requireContext(), MainActivity.home)
                                }
                            }

                            "Pending" -> {
                                binding.recyclerView.apply {
                                    adapter = AdapterHome(requireContext(), pending)
                                }
                            }

                            else -> {
                                binding.recyclerView.apply {
                                    adapter = AdapterHome(requireContext(), requested)
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