package com.amtech.baseetcustomer.MainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.Translator.Translator
import com.amtech.baseetcustomer.databinding.FragmentHomeBinding
import com.amtech.baseetcustomer.databinding.FragmentHomeTabBinding
import com.amtech.baseetcustomer.databinding.FragmentProfileBinding
import com.amtech.vendorservices.V.Dashboard.model.ModelSpinner

class HomeTabFragment : Fragment() {
    private lateinit var  binding: FragmentHomeTabBinding
    private var statisticsList = ArrayList<ModelSpinner>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHomeTabBinding.bind(view)
        statisticsList.clear()
        binding.btnRequest.setOnClickListener {
            startActivity(Intent(requireContext(), Translator::class.java))
        }
        statisticsList.add(ModelSpinner("All", "1"))
        statisticsList.add(ModelSpinner("Requested", "1"))
        statisticsList.add(ModelSpinner("Pending", "1"))
        statisticsList.add(ModelSpinner("Completed", "1"))

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