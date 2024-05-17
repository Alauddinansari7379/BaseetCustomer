package com.amtech.baseetcustomer.MainActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentMyBookingBinding
import com.google.android.material.tabs.TabLayout


class MyBookingFragment : Fragment() {
    private lateinit var  binding: FragmentMyBookingBinding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentMyBookingBinding.bind(view)
         pager = binding.viewPager
        tab = binding.tabs
        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(TranslatorFragment(), "Translator")
        adapter.addFragment(CarFragment(), "Car")
        adapter.addFragment(HomeTabFragment(), "Home")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)

    }
}