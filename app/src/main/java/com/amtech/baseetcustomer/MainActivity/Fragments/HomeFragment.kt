package com.amtech.baseetcustomer.MainActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentHomeBinding
import com.amtech.baseetcustomer.databinding.FragmentProfileBinding

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
    }
}