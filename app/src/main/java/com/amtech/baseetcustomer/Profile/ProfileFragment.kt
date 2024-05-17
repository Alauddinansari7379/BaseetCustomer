package com.amtech.baseetcustomer.Profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var  binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentProfileBinding.bind(view)
        with(binding){
            layoutEdit.setOnClickListener {
                startActivity(Intent(requireContext(),EditProfile::class.java))
            }

            cardChange.setOnClickListener {
                startActivity(Intent(requireContext(),ChangePassword::class.java))
            }

        }

    }
}