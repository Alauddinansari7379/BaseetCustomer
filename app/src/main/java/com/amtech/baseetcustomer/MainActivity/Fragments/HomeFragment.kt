package com.amtech.baseetcustomer.MainActivity.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amtech.baseetcustomer.AddService.CarRental
import com.amtech.baseetcustomer.AddService.HomeRental
import com.amtech.baseetcustomer.AddService.Translator
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var  binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHomeBinding.bind(view)
        with(binding){
             imgHome.setOnClickListener {
                startActivity(Intent(requireContext(),HomeRental::class.java))
            }
            imgTranslator.setOnClickListener {
                startActivity(Intent(requireContext(),Translator::class.java))
            }
            imgCar.setOnClickListener {
                startActivity(Intent(requireContext(),CarRental::class.java))
            }
        }
    }
}