package com.amtech.baseetcustomer.MainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.amtech.baseetcustomer.AddService.BookingDetail
import com.amtech.baseetcustomer.AddService.CarRental
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.AddService.Translator
import com.amtech.baseetcustomer.databinding.FragmentCarBinding
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner

class CarFragment : Fragment() {
    private lateinit var binding: FragmentCarBinding
    private var statisticsList = ArrayList<ModelSpinner>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCarBinding.bind(view)
        statisticsList.clear()
        statisticsList.add(ModelSpinner("All", "1"))
        statisticsList.add(ModelSpinner("Requested", "1"))
        statisticsList.add(ModelSpinner("Pending", "1"))
        statisticsList.add(ModelSpinner("Completed", "1"))

        binding.btnRequest.setOnClickListener {
            startActivity(Intent(requireContext(),CarRental::class.java))
        }

        binding.card1.setOnClickListener {
            startActivity(Intent(requireContext(),BookingDetail::class.java))
        }
        binding.card2.setOnClickListener {
            startActivity(Intent(requireContext(),BookingDetail::class.java))
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
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }


    }
}